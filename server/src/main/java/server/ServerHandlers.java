package server;

import Service.UserService;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import spark.Request;
import spark.Response;

public class ServerHandlers {


    Object loginHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        UserService userService = new UserService();
        var auth = userService.login(user);
        if(auth.authToken() != null){
            res.status(200);
        }
        return new Gson().toJson(auth);
    }
}
