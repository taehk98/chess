package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
  private Integer gameID;
  private ChessMove move;
  public MakeMove(String authToken, Integer gameID, ChessMove move) {
    super(authToken);
    this.gameID = gameID;
    this.move = move;
    this.commandType = CommandType.MAKE_MOVE;
  }

  public Integer getGameID() {
    return gameID;
  }

  public ChessMove getMove() {
    return move;
  }
}
