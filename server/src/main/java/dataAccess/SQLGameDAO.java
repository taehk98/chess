package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.springframework.security.core.parameters.P;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    private static int gameID = 1;
    public SQLGameDAO() {
        try {
            createTable();
        } catch (DataAccessException e) {
        }
    }
    public void createTable()throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){

            var createAuthTokenTable ="""
              CREATE TABLE IF NOT EXISTS chessGame(
                  gameID INT NOT NULL AUTO_INCREMENT,
                  whiteUsername VARCHAR(255),
                  blackUsername VARCHAR(255),
                  gameName VARCHAR(255) NOT NULL,
                  chessGame TEXT,
                  PRIMARY KEY (gameID)
              )""";

            try(var createTableStatement = conn.prepareStatement(createAuthTokenTable)){
                createTableStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new DataAccessException("Data Access Error");
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE chessGame");
            preparedStatement.executeUpdate();
            gameID = 1;
        } catch (SQLException e) {
            throw new DataAccessException("All Game Data Clearing Error");
        }
    }

    @Override
    public int createGame(GameData chessGame) throws DataAccessException {
        ChessGame newChessChessGame= new ChessGame();
        GameData newGame = new GameData(gameID, null, null, chessGame.gameName(), newChessChessGame);
//        var json = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO chessGame (whiteUsername, blackUsername, " +
                     "gameName, chessGame) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, newGame.whiteUsername());
            preparedStatement.setString(2, newGame.blackUsername());
            preparedStatement.setString(3, newGame.gameName());
            Gson gson = new Gson();
            preparedStatement.setString(4, gson.toJson(newGame.game()));
            preparedStatement.executeUpdate();
            gameID++;
            return newGame.gameID();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating chessGame");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM chessGame WHERE gameID=?";
            try( var ps = conn.prepareStatement(statement) ){
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var chessGameJson = rs.getString("chessGame");
                        var chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), chessGame);

                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving chessGame");
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame  FROM chessGame";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                        GameData gameData = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                                rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                        result.add(gameData);
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
            if (playerColor != null && playerColor.equalsIgnoreCase("WHITE") && currGame != null) {
                if (currGame.whiteUsername() == null) {
                    newGame = new GameData(gameID, currAuth.username(), currGame.blackUsername(), currGame.gameName(), currGame.game());
                }else if(currAuth.username() == null){
                    newGame = new GameData(gameID, null, currGame.blackUsername(), currGame.gameName(), currGame.game());
                }else {
                    throw new DataAccessException("Error: already taken");
                }
            } else if (playerColor != null && playerColor.equalsIgnoreCase("BLACK") && currGame != null) {
                if (currGame.blackUsername() == null) {
                    newGame = new GameData(gameID, currGame.whiteUsername(), currAuth.username(), currGame.gameName(), currGame.game());
                }else if(currAuth.username() == null){
                    newGame = new GameData(gameID, currGame.whiteUsername(), null, currGame.gameName(), currGame.game());
                }else {
                    throw new DataAccessException("Error: already taken");
                }
            } else if (playerColor == null) {
                newGame = currGame;
            } else if (currGame == null) {
                throw new DataAccessException("Error: bad request"); //임의로 넣은 에러임
            }


        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE chessGame SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE gameID = ?";
            Gson gson = new Gson();
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, newGame != null ? newGame.whiteUsername() : null);
                ps.setString(2, newGame != null ? newGame.blackUsername() : null);
                ps.setString(3, gson.toJson(newGame != null ? newGame.game() : null));
                ps.setInt(4, newGame != null ? newGame.gameID() : 0);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating Game");
        }
    }

    public void updateGameForMove(int gameID, ChessGame game) throws DataAccessException{
        GameData currGame = getGame(gameID);
        if(game == null){
            throw new DataAccessException("Error: Invalid input");
        }
        GameData newGame = new GameData(gameID, currGame.whiteUsername(), currGame.blackUsername(), currGame.gameName(), game);
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE chessGame SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE gameID = ?";
            Gson gson = new Gson();
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, newGame.whiteUsername());
                ps.setString(2, newGame.blackUsername());
                ps.setString(3, gson.toJson(newGame.game()));
                ps.setInt(4, newGame != null ? newGame.gameID() : 0);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating Game");
        }
    }
}
