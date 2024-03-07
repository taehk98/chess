package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO(){
        try {
//            configureDatabase();
            createTable();
        } catch (DataAccessException e) {
        }
    }
    public void createTable() throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){



            var createAuthTokenTable ="""
              CREATE TABLE IF NOT EXISTS user(
                  email VARCHAR(255) NOT NULL,
                  password VARCHAR(255) NOT NULL,
                  username VARCHAR(255) NOT NULL,
                  PRIMARY KEY (username)
              )""";

            try(var createTableStatement = conn.prepareStatement(createAuthTokenTable)){
                createTableStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new DataAccessException("Data Access Error");
        }
    }
    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        UserData user = null;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
//            var statement = "SELECT username, json FROM user WHERE username=?";
            try( var ps = conn.prepareStatement(statement) ){
                ps.setString(1, userData.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new UserData(rs.getString("username"),rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
            return user;
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
//        var json = new Gson().toJson(userData);
        try (var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
                return userData;
        }

        } catch (SQLException e) {
            throw new DataAccessException("Error creating user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Error clearing user");
        }
    }
//    private UserData readUser(ResultSet rs) throws SQLException {
//        var json = rs.getString("json");
//        return new Gson().fromJson(json, UserData.class);
//    }
//
//    private void configureDatabase() throws DataAccessException {
//        var createStatements =
//                """
//                      CREATE TABLE IF NOT EXISTS user(
//                      username VARCHAR(255) NOT NULL,
//                      json TEXT DEFAULT NULL,
//                      PRIMARY KEY (username)
//                      )""";
//        SQLAuthDAO.configureDatabase(createStatements);
//    }
}
