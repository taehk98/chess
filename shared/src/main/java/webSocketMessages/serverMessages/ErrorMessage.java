package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
  public String error = null;
  public ErrorMessage(ServerMessageType type, String error) {
    super(type);
    this.error = error;
  }
}
