package dataAccess;

import model.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(GameData game) throws DataAccessException;
}
