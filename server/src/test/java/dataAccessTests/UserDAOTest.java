package dataAccessTests;

import Service.UserService;
import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        try(var conn = DatabaseManager.getConnection()) {
            userDAO = new SQLUserDAO();
        } catch (SQLException | DataAccessException e) {
            userDAO = new MemoryUserDAO();
        }
        // UserService 객체 초기화
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("UserDAO createUser Positive test")
    public void PosUserDAOCreateTest() throws DataAccessException {
        userDAO.clear();
        UserData newUser = new UserData("Taehoon", "password", "email");
        userDAO.createUser(newUser);
        UserData user = userDAO.getUser(newUser);
        Assertions.assertEquals(user.username(), newUser.username());
    }

    @Test
    @Order(2)
    @DisplayName("UserDAO createUser Negative test")
    public void NegUserDAOCreateTest() throws DataAccessException {
        userDAO.clear();
        UserData newUser = new UserData("Taehoon", "password", "email");
        userDAO.createUser(newUser);
        UserData user = userDAO.getUser(newUser);
        Assertions.assertNotEquals(user.username(), "Saehee");
    }

    @Test
    @Order(3)
    @DisplayName("UserDAO clear User test")
    public void UserDAOClearTest() throws DataAccessException {
        UserData newUser = new UserData("Taehoon", "password", "email");
        userDAO.createUser(newUser);
        UserData user = userDAO.getUser(newUser);
        Assertions.assertNotNull(user);
        userDAO.clear();
        user = userDAO.getUser(newUser);
        Assertions.assertNull(user);
    }

    @Test
    @Order(4)
    @DisplayName("Pos Tests if getUser returns the correct user")
    public void PosUserDAOGetUserTest() throws DataAccessException {
        UserData newUser = new UserData("Taehoon", "password", "email");
        userDAO.createUser(newUser);
        UserData saehee = new UserData("Saehee", "passwordForSH", "emailForSH");
        userDAO.createUser(saehee);

        UserData user = userDAO.getUser(saehee);
        Assertions.assertNotNull(user);

        Assertions.assertEquals(user.username(), "Saehee");
    }

    @Test
    @Order(5)
    @DisplayName("Neg Tests if getUser returns the correct user")
    public void NegUserDAOGetUserTest() throws DataAccessException {
        UserData newUser = new UserData("Taehoon", "password", "email");
        userDAO.createUser(newUser);
        UserData saehee = new UserData("Saehee", "passwordForSH", "emailForSH");
        userDAO.createUser(saehee);
        UserData random = new UserData("random", "random", "random");
        UserData user = userDAO.getUser(random);

        Assertions.assertNull(user);
    }
}
