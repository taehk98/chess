package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
  public String message = null;

  public NotificationMessage(ServerMessageType type, String message) {
    super(type);
    this.message = message;
  }
}
