package Service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();
    static final GameDAO gameDAO = new SQLGameDAO();

    public UserService() {
    }

    public AuthData register(UserData user) throws DataAccessException {
        UserData returnedUser;
        UserData createdUser;
        AuthData returnedAuth;
        if(user.username() == null || user.email() == null || user.password() == null){
            throw new DataAccessException("Error: bad request");
        }
        returnedUser = userDAO.getUser(user);
        if(returnedUser == null){
            createdUser = userDAO.createUser(user);
        }else {
            throw new DataAccessException("Error: already taken");
        }
        returnedAuth = authDAO.createAuth(createdUser.username());
        return returnedAuth;
    }
    public AuthData login(UserData user) throws DataAccessException {
        AuthData returnedAuth;
        UserData returnedUser;
        returnedUser = userDAO.getUser(user);
        if (returnedUser != null) {
            returnedAuth = authDAO.createAuth(user.username());
        }else {
            throw new DataAccessException("Error: unauthorized");
        }

        return returnedAuth;
    }
    public void logout(AuthData auth) throws DataAccessException {
        AuthData returnedAuth;
        returnedAuth = authDAO.getAuth(auth);
        if(returnedAuth != null) {
            authDAO.delete(auth);
        }else {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    public void clear() throws DataAccessException{
        try{
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }catch(DataAccessException e){
            throw new DataAccessException("Error: description");
        }
    }
}
