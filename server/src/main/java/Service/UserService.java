package Service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    public UserService() {
    }

    public AuthData register(UserData user) {return null;}
    public AuthData login(UserData user) throws DataAccessException {
        AuthData returnedAuth = null;
        UserData returnedUser = memoryUserDAO.getUser(user);
        if (returnedUser != null) {
            returnedAuth = memoryAuthDAO.createAuth(user.username());
        }
        return returnedAuth;
    }
    public void logout(UserData user) {}
}
