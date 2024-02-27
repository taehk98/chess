package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData) throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    void clear() throws DataAccessException;
}
