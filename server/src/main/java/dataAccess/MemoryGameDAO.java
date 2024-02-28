package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    static final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        ChessGame newChessGame = new ChessGame();
        String white = null;
        String black = null;
        GameData newGame = new GameData(nextId++, white, black, game.gameName(), newChessGame);
        games.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(gameID);
    }

    public GameData[] listGames() throws DataAccessException{
        GameData[] gameArray = new GameData[games.size()];
        games.values().toArray(gameArray);
        return gameArray;
    }

    public void updateGame(AuthData currAuth, int gameID, String playerColor) throws DataAccessException {
        GameData game = games.get(gameID);

        if (playerColor != null && playerColor.equals("WHITE")) {
            if (game.whiteUsername() == null) {
                GameData newGame = new GameData(gameID, currAuth.username(), game.blackUsername(), game.gameName(), game.game());
                games.put(game.gameID(), newGame);
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else if (playerColor != null && playerColor.equals("BLACK")) {
            if (game.blackUsername() == null) {
                GameData newGame = new GameData(gameID, game.whiteUsername(), currAuth.username(), game.gameName(), game.game());
                games.put(game.gameID(), newGame);
            }else {
                throw new DataAccessException("Error: already taken");
            }
        }
    }

}
