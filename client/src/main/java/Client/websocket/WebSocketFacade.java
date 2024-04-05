package Client.websocket;

import javax.management.Notification;
import javax.websocket.*;

import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DeploymentException, IOException, URISyntaxException {
    url = url.replace("http", "ws");
    URI socketURI = new URI(url + "/connect");
    this.notificationHandler = notificationHandler;
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, socketURI);

    //set message handler
    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      @Override
      public void onMessage(String message) {
        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
          var builder = new GsonBuilder();
          LoadGameMessage loadGameMessage = builder.create().fromJson(message, LoadGameMessage.class);
          // game 클래스가 아니라?
          notificationHandler.loadGame(loadGameMessage);
        }else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
          NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
          notificationHandler.notify(notificationMessage);
        }else {
          ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
          notificationHandler.error(errorMessage);
        }
      }
    });
  }







  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
}
