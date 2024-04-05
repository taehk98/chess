package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  private boolean isResigned = false;
  private AuthDAO authDAO = new SQLAuthDAO();
  private GameDAO gameDAO = new SQLGameDAO();

  @OnWebSocketError
  public void onError(Throwable error) { error.printStackTrace();}

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException, DataAccessException {
    try {
      UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
      AuthData newAuth = new AuthData(command.getAuthString(), "");
      AuthData authToken = authDAO.getAuth(newAuth);
      String username = authToken.username();
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> joinPlayer(username, session, message, command.getAuthString());
        case JOIN_OBSERVER -> joinObserver(username, session, message, command.getAuthString());
        case MAKE_MOVE -> makeMove(username, session, message, command.getAuthString());
        case LEAVE -> leave(username, session, message, command.getAuthString());
        case RESIGN -> resign(username, session, message, command.getAuthString());
      }
    }catch (Exception e){
      var error ="Command Error. Please check your input!";
      var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, error);
      connections.sendError(errorMessage, session);
    }
  }

  private void joinPlayer(String username, Session session, String message, String authToken) throws IOException, DataAccessException {
    JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
    ChessGame gameData = gameDAO.getGame(joinPlayer.getGameID());

  }
}
