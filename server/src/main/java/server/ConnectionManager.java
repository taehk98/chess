package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
  public final HashMap<Integer, Connection> lobbyConnections = new HashMap<>();

  public void loadGame(ServerMessage game, Session session) throws IOException {
    session.getRemote().sendString(new Gson().toJson(game));
  }

  public void resignMessage(ServerMessage resign, Session session) throws IOException {
    session.getRemote().sendString(new Gson().toJson(resign));
  }

  public void sendError(ServerMessage error, Session session) throws IOException{
    session.getRemote().sendString(new Gson().toJson(error));
  }

  public void add(String authToken, Session session){
    var connection = new Connection(authToken, session);
    connections.put(authToken, connection);
  }

  public void remove(String authToken){
    connections.remove(authToken);
  }

  public void broadcast(String authToken, ServerMessage message) throws IOException {
    var removeList = new ArrayList<Connection>();
    for(var c : connections.values()){
      if(c.session.isOpen()){
        if(!c.authToken.equals(authToken)){
          c.send(new Gson().toJson(message));
        }
      }else{
        removeList.add(c);
      }
    }
    for (var c : removeList){
      connections.remove(c.authToken);
    }
  }
}
