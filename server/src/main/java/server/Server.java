package server;

import spark.Spark;

public class Server {

    private final ServerHandlers serverHandlers;
    private final GameServerHandlers gameHandlers;

    public Server() {
        this.gameHandlers = new GameServerHandlers();
        this.serverHandlers = new ServerHandlers();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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