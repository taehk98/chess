package Service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public UserService() {
    }

    public AuthData register(UserData user) throws DataAccessException {
        UserData returnedUser;
        UserData createdUser;
        AuthData returnedAuth;
        if(user.username() == null || user.email() == null || user.password() == null){
            throw new DataAccessException("Error: bad request");
        }
        returnedUser = memoryUserDAO.getUser(user);
        if(returnedUser == null){
            createdUser = memoryUserDAO.createUser(user);
        }else {
            throw new DataAccessException("Error: already taken");
        }
        returnedAuth = memoryAuthDAO.createAuth(createdUser.username());
        return returnedAuth;
    }
    public AuthData login(UserData user) throws DataAccessException {
        AuthData returnedAuth;
        UserData returnedUser;
        returnedUser = memoryUserDAO.getUser(user);
        if (returnedUser != null) {
            returnedAuth = memoryAuthDAO.createAuth(user.username());
        }else {
            throw new DataAccessException("Error: unauthorized");
        }

        return returnedAuth;
    }
    public void logout(AuthData auth) throws DataAccessException{
        AuthData returnedAuth;
        returnedAuth = memoryAuthDAO.getAuth(auth);
        if(returnedAuth != null) {
            memoryAuthDAO.delete(auth);
        }else {
            throw new DataAccessException("Error: unauthorized");
        }


    }

    public void clear() throws DataAccessException{
        try{
            memoryUserDAO.clear();
            memoryAuthDAO.clear();
            memoryGameDAO.clear();
        }catch(DataAccessException e){
            throw new DataAccessException("Error: description");
        }
    }
}
