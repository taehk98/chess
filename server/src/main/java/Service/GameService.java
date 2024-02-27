package Service;

import chess.ChessPiece;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;

public class GameService {
    static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public GameService(){
    }
    public int createGame(String auth, GameData game) throws DataAccessException {
        if(game.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        var emptyName = "";
        AuthData authData = new AuthData(auth, emptyName);
        AuthData returnedAuth;
        returnedAuth = memoryAuthDAO.getAuth(authData);
        return memoryGameDAO.createGame(game);

    }
}
