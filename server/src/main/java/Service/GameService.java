package Service;

import chess.ChessPiece;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import server.JoinRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class GameService {
    private final AuthDAO authDAO = new SQLAuthDAO();
    static final GameDAO gameDAO = new SQLGameDAO();

    public GameService() {
    }
    public int createGame(AuthData auth, GameData game) throws DataAccessException {
        if(game.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        AuthData returnedAuth;
        returnedAuth = authDAO.getAuth(auth);
        if(returnedAuth != null){
            return gameDAO.createGame(game);
        }else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(AuthData auth, JoinRequest joinInfo) throws DataAccessException {
        String playerColor = joinInfo.getPlayerColor();
        if (playerColor != null && !playerColor.isEmpty() && !playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new DataAccessException("Error: bad request");
        }
        AuthData currAuth = authDAO.getAuth(auth);
        if(currAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        GameData returnedGame;
        returnedGame = gameDAO.getGame(joinInfo.getGameID());
        if(returnedGame != null) {
            gameDAO.updateGame(currAuth, joinInfo.getGameID(), joinInfo.getPlayerColor());
        }else{
            throw new DataAccessException("Error: bad request");
        }
    }

    public GameData[] listGame(AuthData auth) throws DataAccessException {
        AuthData currAuth = authDAO.getAuth(auth);
        if(currAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        ArrayList<GameData> gameList = gameDAO.listGames();
        GameData[] gameArray = new GameData[gameList.size()];
        gameArray = gameList.toArray(gameArray);
        return gameArray;
    }
}
