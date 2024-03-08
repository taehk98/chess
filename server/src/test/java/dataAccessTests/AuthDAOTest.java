package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class AuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        try(var conn = DatabaseManager.getConnection()) {
            authDAO = new SQLAuthDAO();
        } catch (SQLException | DataAccessException e) {
            authDAO = new MemoryAuthDAO();
        }
        // UserService 객체 초기화
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Positively tests CreateAuth if it creates correctly")
    public void PosCreateTest() throws DataAccessException {
        authDAO.clear();
        AuthData newAuth = authDAO.createAuth("taehoon");
        Assertions.assertEquals(newAuth.username(), "taehoon");
    }

    @Test
    @Order(2)
    @DisplayName("Negatively tests CreateAuth if it creates correctly")
    public void NegCreateTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        Assertions.assertNotNull(newAuth.authToken());
    }

    @Test
    @Order(3)
    @DisplayName("Positively tests getAuth if it creates correctly")
    public void PosGetAuthTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        AuthData returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertEquals(newAuth.authToken(), returnedAuth.authToken());
    }

    @Test
    @Order(4)
    @DisplayName("Negatively tests getAuth if it creates correctly")
    public void NegGetAuthTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        AuthData returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertNotNull(returnedAuth.username());
    }

    @Test
    @Order(5)
    @DisplayName("Tests clear() to see if it clears appropriately")
    public void AuthClearTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        AuthData anotherAuth = authDAO.createAuth("saehee");
        AuthData returnedAuth = authDAO.getAuth(newAuth);
        AuthData anotherReturnedAuth = authDAO.getAuth(anotherAuth);
        Assertions.assertNotNull(returnedAuth);
        Assertions.assertNotNull(anotherReturnedAuth);

        authDAO.clear();

        returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertNull(returnedAuth);
        anotherReturnedAuth = authDAO.getAuth(anotherReturnedAuth);
        Assertions.assertNull(anotherReturnedAuth);
    }

    @Test
    @Order(6)
    @DisplayName("Positively Tests AuthDelete() to see if it clears appropriately")
    public void PosAuthDeleteTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        authDAO.createAuth("saehee");
        AuthData returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertNotNull(returnedAuth);

        authDAO.delete(newAuth);

        returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertNull(returnedAuth);
    }

    @Test
    @Order(7)
    @DisplayName("Negatively Tests AuthDelete() to see if it clears appropriately")
    public void NegAuthDeleteTest() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("taehoon");
        AuthData anotherAuth = authDAO.createAuth("saehee");
        AuthData returnedAuth = authDAO.getAuth(newAuth);
        Assertions.assertNotNull(returnedAuth);

        authDAO.delete(newAuth);

        AuthData anotherReturnedAuth = authDAO.getAuth(anotherAuth);
        Assertions.assertNotNull(anotherReturnedAuth);
    }
}
