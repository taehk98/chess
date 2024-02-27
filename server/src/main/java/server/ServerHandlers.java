package server;

import Service.UserService;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;

public class ServerHandlers {
    UserService userService = new UserService();
    RegisterRes errorRes = new RegisterRes();

    Object loginHandler(Request req, Response res) throws DataAccessException {
        errorRes = new RegisterRes();
        var user = new Gson().fromJson(req.body(), UserData.class);
        try {
            var auth = userService.login(user);
            res.status(200);
            return new Gson().toJson(auth);
        }catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")){
                res.status(401);
            }else{
                res.status(500);
            }
            errorRes.setMessage(e.getMessage());
        }
        return new Gson().toJson(errorRes);
    }

    Object registerHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = null;
        try {
            auth = userService.register(user);
            res.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: bad request")){
                res.status(400);
            } else if(e.getMessage().equals("Error: already taken")) {
                res.status(403);
            } else {
                res.status(500);
            }
            errorRes.setMessage(e.getMessage());
        }
        return new Gson().toJson(errorRes);
    }

    Object clearHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        try {
            userService.clear();
            res.status(200);
        } catch (DataAccessException e) {
            errorRes.setMessage(e.getMessage());
            res.status(500);
        }
        return new Gson().toJson(errorRes);
    }

    Object logoutHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        var auth = req.headers("authorization");
        var emptyName = "";
        AuthData authData = new AuthData(auth, emptyName);
        try {
            userService.logout(authData);
            res.status(200);
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")){
                res.status(401);
            } else {
                res.status(500);
            }
            errorRes.setMessage(e.getMessage());
        }
        return new Gson().toJson(errorRes);
    }

    Object createGameHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        var auth = req.headers("authorization");
    }
}
