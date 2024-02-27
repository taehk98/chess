package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    void delete(AuthData user) throws DataAccessException;

    AuthData getAuth(AuthData user) throws DataAccessException;
}
