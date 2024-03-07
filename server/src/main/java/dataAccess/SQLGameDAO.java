package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    private static int nextId = 1;

    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game");
            preparedStatement.executeUpdate();
            nextId = 1;
        } catch (SQLException e) {
            throw new DataAccessException("All Game Data Clearing Error");
        }
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        ChessGame newChessGame = new ChessGame();
        GameData newGame = new GameData(nextId++, null, null, game.gameName(), newChessGame);
        var json = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO game (gameID, json) VALUES (?, ?)")) {
            preparedStatement.setInt(1, newGame.gameID());
            preparedStatement.setString(2, json);
            preparedStatement.executeUpdate();
            return newGame.gameID();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM game WHERE gameID=?";
            try( var ps = conn.prepareStatement(statement) ){
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game");
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing all games");
        }
        return result;
    }

    @Override
    public void updateGame(AuthData currAuth, int gameID, String playerColor) throws DataAccessException {
        GameData currGame = getGame(gameID);
        GameData newGame = null;
        if (playerColor != null && playerColor.equals("WHITE")) {
            if (currGame.whiteUsername() == null) {
                newGame = new GameData(gameID, currAuth.username(), currGame.blackUsername(), currGame.gameName(), currGame.game());
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else if (playerColor != null && playerColor.equals("BLACK")) {
            if (currGame.blackUsername() == null) {
                newGame = new GameData(gameID, currGame.whiteUsername(), currAuth.username(), currGame.gameName(), currGame.game());
            }else {
                throw new DataAccessException("Error: already taken");
            }
        }

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET json = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                var json = new Gson().toJson(newGame);
                ps.setString(2, json);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating Game");
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    private final String[] createStatements = {
            """
                  CREATE TABLE IF NOT EXISTS game(
                  gameID VARCHAR(255) NOT NULL,
                  json TEXT DEFAULT NULL,
                  PRIMARY KEY (gameID)
                  )"""
    };

    private void configureDatabase() throws DataAccessException {
        SQLAuthDAO.configureDatabase(createStatements);
    }
}
