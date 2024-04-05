package Client.websocket;

import webSocketMessages.serverMessages.*;

public interface NotificationHandler {
  void notify(NotificationMessage notification);
  void error(ErrorMessage errorMessage);

  void loadGame(LoadGameMessage loadGameMessage);
}