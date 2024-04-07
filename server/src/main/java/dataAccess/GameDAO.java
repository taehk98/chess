package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(GameData game) throws DataAccessException;

   GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(AuthData currAuth, int gameID, String playerColor) throws DataAccessException;

    void updateGameForMove(int gameID, ChessGame game) throws DataAccessException;
}
