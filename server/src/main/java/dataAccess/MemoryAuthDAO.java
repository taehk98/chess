package dataAccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    public MemoryAuthDAO() {

    }
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }
}
