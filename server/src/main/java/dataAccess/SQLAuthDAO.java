package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO()  {
        try {
//            configureDatabase();
            createTable();
        } catch (DataAccessException e) {
        }
    }
    public void createTable()throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            DatabaseManager.createDatabase();

            conn.setCatalog("chess");

            var createAuthTokenTable ="""
              CREATE TABLE IF NOT EXISTS auth(
                  authToken VARCHAR(255) NOT NULL,
                  username VARCHAR(255) NOT NULL,
                  PRIMARY KEY (authToken)
              )""";

            try(var createTableStatement = conn.prepareStatement(createAuthTokenTable)){
                createTableStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new DataAccessException("Data Access Error");
        }
    }
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        var conn = DatabaseManager.getConnection();
        try(var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")){
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new DataAccessException("Error: Data Access Exception");
        }
        return new AuthData(token, username);
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
            var ps = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken=?");
            ps.setString(1, user.authToken());
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
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


//    private void configureDatabase() throws DataAccessException {
//        var createStatements ="""
//                   CREATE TABLE IF NOT EXISTS auth(
//                  authToken VARCHAR(255) NOT NULL,
//                  json TEXT DEFAULT NULL,
//                  PRIMARY KEY (authToken)
//                  )""";
//        configureDatabase(createStatements);
//    }
//
//    static void configureDatabase(String createStatements) throws DataAccessException {
//
//        try (var conn = DatabaseManager.getConnection()) {
//            DatabaseManager.createDatabase();
//            conn.setCatalog("chess");
//                try (var preparedStatement = conn.prepareStatement(createStatements)) {
//                    preparedStatement.executeUpdate();
//                }
//        } catch (SQLException ex) {
//            throw new DataAccessException("Unable to configure database");
//        }
//    }
}
