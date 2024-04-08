package server;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

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
      var error ="Command Error. Please check your input or verify if you're next in line.";
      var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, error);
      connections.sendError(errorMessage, session);
    }
  }

  private void joinPlayer(String username, Session session, String message, String authToken) throws IOException, DataAccessException {
    JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
    GameData gameData = gameDAO.getGame(joinPlayer.getGameID());
    ChessGame game= gameData.game();

    if ((gameData.whiteUsername() != null && joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE && gameData.whiteUsername().equalsIgnoreCase(username))
            || (gameData.blackUsername() != null && joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK && gameData.blackUsername().equalsIgnoreCase(username))) {
      connections.add(joinPlayer.getAuthString(), session);
      var notification = String.format("%s joined the game as a player (%s).", username, joinPlayer.getPlayerColor());
      var newNotif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
      connections.broadcast(authToken, newNotif);
      isResigned = false;
      var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
      connections.loadGame(loadGame, session);
    }else{
      throw new IOException("Failed to join");
    }
  }

  private void joinObserver(String username, Session session, String message, String authToken) throws IOException, DataAccessException {
    JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
    GameData gameData = gameDAO.getGame(joinObserver.getGameID());
    ChessGame game= gameData.game();

    connections.add(joinObserver.getAuthString(), session);
    var notification = String.format("%s joined the game as an observer.", username);
    var newNotif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
    connections.broadcast(authToken, newNotif);
    // Load game
    var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    connections.loadGame(loadGame, session);
  }

  private void makeMove(String username, Session session, String message, String authToken) throws IOException, DataAccessException, InvalidMoveException {
    if (!isResigned) {
      MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
      ChessPosition startPosition = makeMove.getMove().getStartPosition();
      ChessPosition endPosition = makeMove.getMove().getEndPosition();
      GameData dataGame = gameDAO.getGame(makeMove.getGameID());
      ChessGame chessGame= dataGame.game();
      //
      if ((dataGame.whiteUsername().equalsIgnoreCase(username)
              && chessGame.getBoard().getPiece(startPosition).getTeamColor()
              == ChessGame.TeamColor.WHITE)
       || (dataGame.blackUsername().equalsIgnoreCase(username)
              && chessGame.getBoard().getPiece(startPosition).getTeamColor()
              == ChessGame.TeamColor.BLACK)){
        chessGame.makeMove(makeMove.getMove());
        AuthData auth = new AuthData(authToken, username);
        gameDAO.updateGameForMove(makeMove.getGameID(), chessGame);

        var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
        var notification = String.format("%s moved the piece from %s to %s", username, startPosition.toString(), endPosition.toString());
        var newNotif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);

        connections.loadGame(loadGame, session);
        connections.broadcast(authToken, newNotif);
        connections.broadcast(authToken, loadGame);

        if (chessGame.isInCheckmate(chessGame.getTeamTurn())){
          String team = chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK ? "White" : "Black";
          var checkNotifMessage = String.format("Chess Mate by %s", team);
          var checkNotif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkNotifMessage);
          connections.resignMessage(checkNotif, session);
          connections.broadcast(authToken, checkNotif);
          isResigned = true;
        } else if (chessGame.isInCheck(chessGame.getTeamTurn())) {
          String team=chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK ? "White" : "Black";
          var checkNotifMessage=String.format("Check by %s", team);
          var checkNotif= new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkNotifMessage);
          connections.resignMessage(checkNotif, session);
          connections.broadcast(authToken, checkNotif);
        }
      }else if (!dataGame.blackUsername().equals(username) && !dataGame.whiteUsername().equals(username)){
        // error
        throw new InvalidMoveException();
      }else {
        throw new InvalidMoveException();
      }
    }else{
      throw new IOException();
    }
  }

  private void leave(String username, Session session, String message, String authToken) throws IOException, DataAccessException {
    Leave leave = new Gson().fromJson(message, Leave.class);
    GameData data = gameDAO.getGame(leave.getGameID());
    System.out.println(leave.getGameID());

    if (data.whiteUsername() != null && data.whiteUsername().equalsIgnoreCase(username)) {
      AuthData auth = new AuthData(authToken, null);
      gameDAO.updateGame(auth, leave.getGameID(), "White");
      connections.remove(authToken);
    }else if (data.blackUsername() != null && data.blackUsername().equalsIgnoreCase(username)) {
      AuthData auth = new AuthData(authToken, null);
      gameDAO.updateGame(auth, leave.getGameID(), "Black");
      connections.remove(authToken);
    }else {
      connections.remove(authToken);
    }
    var notification = String.format("%s left the game.", username);
    var notif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
    connections.broadcast(authToken, notif);
  }

  private void resign(String username, Session session, String message, String authToken) throws IOException, DataAccessException {
    Resign resign = new Gson().fromJson(message, Resign.class);
    GameData data = gameDAO.getGame(resign.getGameID());
    ChessGame chessGame= data.game();
    if (isResigned) {
      throw new IOException();
    } else {
      if(!(data.whiteUsername().equalsIgnoreCase(username)) && !(data.blackUsername().equalsIgnoreCase(username))){
        throw new IOException();
      }
      else {
        isResigned = true;
        var notification = String.format("%s resigned the game.", username);
        var notif = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
        connections.resignMessage(notif, session);
        connections.broadcast(authToken, notif);
      }
    }
  }
}
