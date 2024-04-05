package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
  private Integer gameID;
  private ChessGame.TeamColor playerColor;

  public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.gameID=gameID;
    this.commandType = CommandType.JOIN_PLAYER;
    this.playerColor=playerColor;
  }


  public Integer getGameID() {
    return gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }
}