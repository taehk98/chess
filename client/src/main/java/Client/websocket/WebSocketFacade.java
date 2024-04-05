package Client.websocket;

import javax.management.Notification;
import javax.websocket.*;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import webSocketMessages.serverMessages.NotificationMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
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
          NotificationMessage notification = new Gson().fromJson(message, Notification.class);
          notificationHandler.notify(notification);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }







  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
}
