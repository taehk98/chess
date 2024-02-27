package Service;

import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData user) {}
    public AuthData login(UserData user) {
        return userDAO.getUser(user.username());
    }
    public void logout(UserData user) {}
}
