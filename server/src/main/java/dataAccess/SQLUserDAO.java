package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
        }
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try( var ps = conn.prepareStatement(statement) ){
                ps.setString(1, userData.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user");
        }
        return null;
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        var json = new Gson().toJson(userData);
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO user (username, json) VALUES (?, ?)")) {
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, json);
            preparedStatement.executeUpdate();
            return userData;
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

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    private final String[] createStatements = {
            """
                  CREATE TABLE IF NOT EXISTS user(
                  username VARCHAR(255) NOT NULL,
                  password VARCHAR(255) NOT NULL,
                  email VARCHAR(255) NOT NULL,
                  PRIMARY KEY (username)
                  )"""
    };

    private void configureDatabase() throws DataAccessException {
        SQLAuthDAO.configureDatabase(createStatements);
    }
}
