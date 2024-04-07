package Client.websocket;

import javax.websocket.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
  Session session;
  NotificationHandler notificationHandler;


  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DeploymentException, IOException, URISyntaxException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          System.out.println("here in on Message");
          ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
          if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            var builder = new GsonBuilder();
//            builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
//            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
//            builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
//            LoadGameMessage loadGameMessage = builder.create().fromJson(message, LoadGameMessage.class);
            LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
            System.out.println("here in on Message");

            notificationHandler.loadGame(loadGameMessage);
          }else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println("here in notification");
            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
            notificationHandler.notify(notificationMessage);
          }else {
            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
            notificationHandler.error(errorMessage);
          }
        }
      });
    }catch (DeploymentException | IOException | URISyntaxException ex){
      throw ex;
    }

  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }

  public void joinGameAsPlayer(String authToken, Integer gameID, String color) throws IOException {
    ChessGame.TeamColor playerColor =
            color.equalsIgnoreCase("white")
                    ? ChessGame.TeamColor.WHITE
                    : ChessGame.TeamColor.BLACK;
    try{
      var command = new JoinPlayer(authToken, gameID, playerColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }catch(IOException ex) {
      throw new IOException("Failed to join as a player");
    }
  }

  public void joinGameAsObserver(String authToken, Integer gameID) throws IOException {
    var command = new JoinObserver(authToken, gameID);
    this.session.getBasicRemote().sendText(new Gson().toJson(command));
  }

  public void leaveGame(String authToken, Integer gameID) throws IOException {
    var command = new Leave(authToken, gameID);
    this.session.getBasicRemote().sendText(new Gson().toJson(command));
  }

  public void makeGameMove(String authToken, Integer gameID, ChessMove move) throws IOException {
    var command = new MakeMove(authToken, gameID, move);
    this.session.getBasicRemote().sendText(new Gson().toJson(command));
  }

  public void resignGame(String authToken, Integer gameID) throws IOException {
    var command = new Resign(authToken, gameID);
    this.session.getBasicRemote().sendText(new Gson().toJson(command));
  }
}
