package serviceTests;

import Service.GameService;
import Service.UserService;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.JoinRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {
    private GameService gameService;
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private MemoryGameDAO memoryGameDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();
        memoryGameDAO = new MemoryGameDAO();
        userService = new UserService();
        gameService = new GameService();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        memoryGameDAO.clear();
        memoryUserDAO.clear();
        memoryAuthDAO.clear();
    }

    @Test
    void createGame_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        AuthData authData = userService.register(validUserData);
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(12345, null,null,"validGameName",chessGame);
        int gameID = gameService.createGame(authData, game);
        assertEquals(gameID, 1);
    }
    @Test
    void createGame_negativeCase() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        AuthData authData = new AuthData("authToken", "userName");
        GameData game = new GameData(12345, null,null,null,chessGame);

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authData, game);
        });
        String expectedMessage = "Error: bad request";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void joinGame_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        AuthData authData = userService.register(validUserData);
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(12345, null,null,"validGameName",chessGame);

        int gameID = gameService.createGame(authData, game);

        JoinRequest joinInfo = new JoinRequest("WHITE", gameID);

        gameService.joinGame(authData, joinInfo);

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(authData, joinInfo);
        });

        String expectedMessage = "Error: already taken";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void joinGame_negativeCase() throws DataAccessException
    {
        AuthData authData = new AuthData("authToken", "userName");
        JoinRequest joinInfo = new JoinRequest("WHITE", 2);
        Throwable exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(authData, joinInfo);
        });
        String expectedMessage = "Error: unauthorized";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void listGame_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        AuthData authData = userService.register(validUserData);
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(12345, null,null,"validGameName",chessGame);
        int gameID = gameService.createGame(authData, game);
        GameData[] games = gameService.listGame(authData);

        assertEquals(games.length, 1);
    }
    @Test
    void listGame_negativeCase() throws DataAccessException {
        AuthData authData = new AuthData("authToken", "userName");

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            gameService.listGame(authData);
        });
        String expectedMessage = "Error: unauthorized";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}