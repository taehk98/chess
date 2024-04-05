package server;

import dataAccess.DataAccessException;
import spark.Spark;

public class Server {

    ServerHandlers serverHandlers = new ServerHandlers();
    GameServerHandlers gameHandlers = new GameServerHandlers();
    WebSocketHandler webSocketHandler = new WebSocketHandler();

    public Server(){
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", serverHandlers::loginHandler);
        Spark.post("/user", serverHandlers::registerHandler);
        Spark.delete("/db", serverHandlers::clearHandler);
        Spark.delete("/session", serverHandlers::logoutHandler);
        Spark.post("/game", gameHandlers::createGameHandler);
        Spark.put("/game", gameHandlers::joinGameHandler);
        Spark.get("/game", gameHandlers::listGameHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}