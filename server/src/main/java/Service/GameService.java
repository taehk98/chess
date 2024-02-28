package Service;

import chess.ChessPiece;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import server.JoinRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameService {
    static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public GameService(){
    }
    public int createGame(AuthData auth, GameData game) throws DataAccessException {
        if(game.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        AuthData returnedAuth;
        returnedAuth = memoryAuthDAO.getAuth(auth);
        if(returnedAuth != null){
            return memoryGameDAO.createGame(game);
        }else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(AuthData auth, JoinRequest joinInfo) throws DataAccessException {
        String playerColor = joinInfo.getPlayerColor();
        if (playerColor != null && !playerColor.isEmpty() && !playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new DataAccessException("Error: bad request");
        }
        AuthData currAuth = memoryAuthDAO.getAuth(auth);
        if(currAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        GameData returnedGame;
        returnedGame = memoryGameDAO.getGame(joinInfo.getGameID());
        if(returnedGame != null) {
            memoryGameDAO.updateGame(currAuth, joinInfo.getGameID(), joinInfo.getPlayerColor());
        }else{
            throw new DataAccessException("Error: bad request");
        }
    }

    public ArrayList<GameData> listGame(AuthData auth) throws DataAccessException {
        AuthData currAuth = memoryAuthDAO.getAuth(auth);
        if(currAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return memoryGameDAO.listGames();
    }
}
