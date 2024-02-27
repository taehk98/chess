package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private int nextId = 1;
    final private HashMap<String, UserData> users = new HashMap<>();
    public MemoryUserDAO() {
    }
    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        return null;
    }
}
