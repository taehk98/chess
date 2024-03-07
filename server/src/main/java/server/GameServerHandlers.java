package server;

import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameServerHandlers {
    GameService gameService = new GameService();
    RegisterRes errorRes = new RegisterRes();
    Object createGameHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        CreateGameResponse successResponse = new CreateGameResponse();
        var auth = req.headers("authorization");
        var gameName = new Gson().fromJson(req.body(), GameData.class);
        var emptyName = "";
        AuthData authData = new AuthData(auth, emptyName);
        try{
            int gameID = gameService.createGame(authData, gameName);
            res.status(200);
            successResponse.setGameID(gameID);
            return new Gson().toJson(successResponse);
        } catch (DataAccessException e) {
            handleDataAccessException(e, res);
        }
        return new Gson().toJson(errorRes);
    }
    Object joinGameHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        var auth = req.headers("authorization");
        var join = new Gson().fromJson(req.body(), JoinRequest.class);
        var emptyName = "";
        AuthData authData = new AuthData(auth, emptyName);
        try{
            gameService.joinGame(authData, join);
            res.status(200);
        } catch (DataAccessException e) {
            handleDataAccessException(e, res);
        }
        return new Gson().toJson(errorRes);
    }

    Object listGameHandler(Request req, Response res) {
        errorRes = new RegisterRes();
        var auth = req.headers("authorization");
        ListGameResponse gameLists = new ListGameResponse();
        var emptyName = "";
        AuthData authData = new AuthData(auth, emptyName);
        try{
            ArrayList<GameData> games = gameService.listGame(authData);
            res.status(200);
            if(games != null){
                gameLists.setGameList(games);
                return new Gson().toJson(gameLists);
            }
        } catch (DataAccessException e) {
            handleDataAccessException(e, res);
        }
        return new Gson().toJson(errorRes);
    }

    private void handleDataAccessException(DataAccessException e, Response res) {
        if (e.getMessage().equals("Error: bad request")) {
            res.status(400);
        } else if (e.getMessage().equals("Error: unauthorized")) {
            res.status(401);
        } else if (e.getMessage().equals("Error: already taken")) {
            res.status(403);
        } else {
            res.status(500);
        }
        errorRes.setMessage(e.getMessage());
    }
}
