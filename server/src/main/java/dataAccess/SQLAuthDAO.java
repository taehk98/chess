package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO()  {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
        }
    }
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        var json = new Gson().toJson(newAuth);
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, json) VALUES (?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, json);
            preparedStatement.executeUpdate();
            return newAuth;
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("All Auth Data Clearing Error");
        }
    }

    @Override
    public void delete(AuthData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?");
            preparedStatement.setString(1, user.authToken());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("An Auth Data deleting Error");
        }
    }

    @Override
    public AuthData getAuth(AuthData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT authToken, json FROM auth WHERE authToken=?");
            ps.setString(1, user.authToken());
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readAuth(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Auth Data retrieving Error");
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    private final String[] createStatements = {
            """
                  CREATE TABLE IF NOT EXISTS auth(
                  authToken VARCHAR(255) NOT NULL,
                  json TEXT DEFAULT NULL,
                  PRIMARY KEY (authToken)
                  )"""
    };

    private void configureDatabase() throws DataAccessException {
        configureDatabase(createStatements);
    }

    static void configureDatabase(String[] createStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chess");
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }
}
