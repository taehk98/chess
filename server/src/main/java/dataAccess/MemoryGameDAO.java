package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        ChessGame newChessGame = new ChessGame();
        String white = "";
        String black = "";
        GameData newGame = new GameData(nextId++, white, black, game.gameName(), newChessGame);
        games.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

}
