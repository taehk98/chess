package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(GameData game) throws DataAccessException;

   GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(AuthData currAuth, int gameID, String playerColor) throws DataAccessException;
}
