package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    static private final HashMap<String, UserData> users = new HashMap<>();
    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        UserData storedUser = users.get(userData.username());
        if(storedUser != null && storedUser.password().equals(userData.password())) {
            return storedUser;
        }else{
            return null;
        }
    }

    @Override
    public UserData createUser(UserData userData) {
        UserData newUser = new UserData(userData.username(), userData.password(), userData.email());
        users.put(newUser.username(), newUser);
        return newUser;
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
