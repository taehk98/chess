package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
  private Integer gameID;
  public Leave(String authToken, Integer gameID) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = CommandType.LEAVE;
  }

  public Integer getGameID() {
    return gameID;
  }
}
