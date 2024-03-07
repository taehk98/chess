package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        try(var conn = DatabaseManager.getConnection()) {
            gameDAO = new SQLGameDAO();
            gameDAO.clear();
        } catch (SQLException | DataAccessException e) {
            gameDAO = new MemoryGameDAO();
        }
        // UserService 객체 초기화
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("gameDAO createGame Positive test")
    public void PosCreateTest() throws DataAccessException {
        gameDAO.clear();
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertEquals(returnedGame.gameName(), "gameNameEX");
    }
    @Test
    @Order(2)
    @DisplayName("gameDAO createGame negative test")
    public void NegCreateTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotEquals(returnedGame.gameID(), 123);
    }

    @Test
    @Order(3)
    @DisplayName("gameDAO getGame positive test")
    public void PosGetGameTest() throws DataAccessException {
        GameData returnedNull = gameDAO.getGame(1);
        Assertions.assertNull(returnedNull);
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(returnedGame);
    }

    @Test
    @Order(4)
    @DisplayName("gameDAO getGame negative test")
    public void NegGetGameTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(returnedGame);
    }

    @Test
    @Order(5)
    @DisplayName("Negatively Tests if listGames works")
    public void NegListGamesTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData anotherGame = new GameData(456, null, null, "anotherGameName", game);
        gameDAO.createGame(anotherGame);
        ArrayList<GameData> lists = gameDAO.listGames();
        Assertions.assertNotEquals(lists.size(), 1);
    }

    @Test
    @Order(6)
    @DisplayName("Positively Tests if listGames works")
    public void PosListGamesTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData anotherGame = new GameData(456, null, null, "anotherGameName", game);
        gameDAO.createGame(anotherGame);
        ArrayList<GameData> lists = gameDAO.listGames();
        Assertions.assertEquals(lists.get(1).gameName(), "anotherGameName");
    }

    @Test
    @Order(7)
    @DisplayName("Tests clear() to see if it clears appropriately")
    public void ClearTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(returnedGame);
        gameDAO.clear();
        returnedGame = gameDAO.getGame(1);
        Assertions.assertNull(returnedGame);
    }

    @Test
    @Order(8)
    @DisplayName("Positively Tests UpdateGame() to see if it updates appropriately")
    public void PosUpdateGameTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(returnedGame);
        AuthData auth = new AuthData("auth", "WhiteUserName");
        gameDAO.updateGame(auth, 1, "WHITE");
        returnedGame = gameDAO.getGame(1);
        Assertions.assertEquals(returnedGame.whiteUsername(), "WhiteUserName");
    }

    @Test
    @Order(9)
    @DisplayName("Negatively Tests UpdateGame() to see if it updates appropriately")
    public void NegUpdateGameTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(123, null, null, "gameNameEX", game);
        gameDAO.createGame(newGame);
        GameData returnedGame = gameDAO.getGame(1);
        Assertions.assertNotNull(returnedGame);
        AuthData auth = new AuthData("auth", "WhiteUserName");

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(auth, 2, "WHITE");
        });

        String expectedMessage = "Error: bad request";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
