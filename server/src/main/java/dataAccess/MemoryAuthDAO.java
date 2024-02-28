package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    static private final HashMap<String, AuthData> authTokens = new HashMap<>();
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(token, username);
        authTokens.put(token, newAuth);
        return newAuth;
    }

    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
    }

    @Override
    public void delete(AuthData auth) throws DataAccessException {
        AuthData storedToken = authTokens.get(auth.authToken());
        if(storedToken != null){
            authTokens.remove(auth.authToken(), storedToken);
        }
    }

    @Override
    public AuthData getAuth(AuthData auth) throws DataAccessException {
        return authTokens.get(auth.authToken());
    }
}
