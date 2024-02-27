package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<String, GameData> games = new HashMap<>();
    public MemoryGameDAO() {

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
