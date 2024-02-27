package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData) throws DataAccessException;
}
