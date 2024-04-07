package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
  public String errorMessage = null;
  public ErrorMessage(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessage = errorMessage;
  }
}
