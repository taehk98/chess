package clientTests;

import Client.ServerFacade;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
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
    void posRegister() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        var authData = facade.addUser(user);
        assertTrue(authData.authToken().length() > 10);

    }

    @Test
    void negRegister() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        var authData = facade.addUser(user);
        Assertions.assertNotEquals(authData.username() , "player4");
    }

    @Test
    void posLogin() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        facade.addUser(user);
        AuthData auth = facade.getUser(new UserData("player3", "password", null));
        Assertions.assertEquals(auth.username(), "player3");

    }

    @Test
    void negLogin() throws Exception {
        UserData user = new UserData("player3", "password", "p1@email.com");
        facade.addUser(user);

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            AuthData auth = facade.getUser(new UserData("player3", "Wrongpassword", null));
        });

        String expectedMessage = "Request Error";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

}
