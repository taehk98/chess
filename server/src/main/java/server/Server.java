package server;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import Service.UserService;
import spark.*;

import java.security.Provider;

public class Server {

    private final ServerHandlers serverHandlers;

    public Server() {
        this.serverHandlers = new ServerHandlers();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", serverHandlers::loginHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}