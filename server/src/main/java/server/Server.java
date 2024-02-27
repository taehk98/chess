package server;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import Service.UserService;
import spark.*;

import java.security.Provider;

public class Server {
    private final UserService userService;
//    private final Service.AuthService authService;

//    public Server(AuthDAO authDao) {
//        authService = new Service.AuthService(authDao);
//    }

    public Server() {
        userService = new UserService();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object loginHandler(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = userService.login(user);
        return new Gson().toJson(auth);
    }
}