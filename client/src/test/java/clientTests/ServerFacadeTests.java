package clientTests;

import Client.ServerFacade;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import RequestResponses.CreateGameResponse;
import RequestResponses.JoinRequest;
import RequestResponses.ListGameResponse;
import server.Server;


import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        facade.clear();
    }

    @AfterEach
    public void between() throws DataAccessException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void posClearTest() throws Exception {
        UserData user1 = new UserData("user1", "password", "p1@email.com");
        UserData user2 = new UserData("user2", "password", "p1@email.com");
        facade.addUser(user1);
        facade.addUser(user2);
        AuthData auth = facade.getUser(new UserData("user2", "password", null));
        Assertions.assertEquals(auth.username(), "user2");

        facade.clear();

        Throwable exception = assertThrows(DataAccessException.class, () -> facade.getUser(new UserData("user2", "password", null)));

        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void negClearTest() throws Exception {
        UserData user1 = new UserData("user1", "password", "p1@email.com");
        UserData user2 = new UserData("user2", "password", "p1@email.com");
        facade.addUser(user1);
        AuthData auth = facade.addUser(user2);

        facade.clear();

        Throwable exception = assertThrows(DataAccessException.class, () -> facade.createGame(auth, new GameData(0, null, null, "newGame", null)));

        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void posRegisterTest() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        var authData = facade.addUser(user);
        assertTrue(authData.authToken().length() > 10);

    }

    @Test
    void negRegisterTest() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        var authData = facade.addUser(user);
        Assertions.assertNotEquals(authData.username() , "player4");
    }

    @Test
    void posLoginTest() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        facade.addUser(user);
        AuthData auth = facade.getUser(new UserData("player3", "password", null));
        Assertions.assertEquals(auth.username(), "player3");

    }

    @Test
    void negLoginTest() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        facade.addUser(user);

        Throwable exception = assertThrows(DataAccessException.class, () -> facade.getUser(new UserData("player3", "Wrong-password", null)));

        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void posLogOutTest() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);
        assertNotNull(auth);

        facade.deleteUser(auth);

        Throwable exception = assertThrows(DataAccessException.class, () -> facade.createGame(auth, new GameData(0, null, null, "newGame", null)));
        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void negLogOutTest() {
        Throwable exception = assertThrows(DataAccessException.class, () -> facade.deleteUser(new AuthData("notExists", "wrong")));
        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void posCreateGameTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        CreateGameResponse res = facade.createGame(auth, new GameData(0, null,null, "newGame", null));

        Assertions.assertEquals(res.getGameID(), 1);
    }

    @Test
    void negCreateGameTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        CreateGameResponse res = facade.createGame(auth, new GameData(0, null,null, "newGame", null));

        Assertions.assertNotEquals(res.getGameID(), 1);
    }

    @Test
    void posListGamesTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        ListGameResponse res = facade.listGames(auth);
        Assertions.assertEquals(res.getGamesString(), "Game ID: 1, White Username: null, Black Username: null, Game Name: newGame\n");
    }

    @Test
    void negListGamesTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        ListGameResponse games = facade.listGames(auth);
        Assertions.assertNotEquals(games.getGamesString(), "Game ID: 1, White Username: null, Black Username: null, Game Name: newGame\n");
    }

    @Test
    void posJoinGameTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        facade.joinGame(auth, new JoinRequest("white", 1));
        ListGameResponse res = facade.listGames(auth);

        Assertions.assertEquals(res.getGamesString(), "Game ID: 1, White Username: player3, Black Username: null, Game Name: newGame\n");
    }

    @Test
    void negJoinGameTest() throws DataAccessException {
        UserData user = new UserData("player3", "password", "p1@email.com");
        AuthData auth = facade.addUser(user);

        facade.createGame(auth, new GameData(0, null,null, "newGame", null));
        facade.joinGame(auth, new JoinRequest("white", 1));

        Throwable exception = assertThrows(DataAccessException.class, () -> facade.joinGame(auth, new JoinRequest("white", 1)));

        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);


    }



}
