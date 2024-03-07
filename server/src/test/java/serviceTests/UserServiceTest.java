package serviceTests;

import Service.UserService;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;
import server.Server;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;


    @BeforeEach
    void setUp() {
        userService = new UserService();
        try(var conn = DatabaseManager.getConnection()) {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (SQLException | DataAccessException e) {
            userDAO = new MemoryUserDAO();
            authDAO = new MemoryAuthDAO();
        }
        // UserService 객체 초기화

        // 테스트 시작 전에 users 해시맵을 초기화합니다.
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }


    @Test
    void register_PositiveCase() throws DataAccessException {
        // 유효한 사용자 데이터 생성
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");

        // 테스트
        AuthData authData = userService.register(validUserData);

        // 예상 결과와 실제 결과 비교
        assertNotNull(authData);
        assertEquals(authData.username(), "validUsername");
    }
    @Test
    void register_negativeCase() throws DataAccessException {
        UserData invalidUserData = new UserData("validUsername", "validEmail", null);

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            AuthData authData = userService.register(invalidUserData);
        });

        String expectedMessage = "Error: bad request";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void login_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        userService.register(validUserData);
        AuthData authData = userService.login(validUserData);

        assertNotNull(authData);
        assertEquals(authData.username(), "validUsername");
    }

    @Test
    void login_negativeCase() throws DataAccessException {
        UserData invalidUserData = new UserData("validUsername", "validEmail", null);

        Throwable exception = assertThrows(DataAccessException.class, () -> {
            userService.login(invalidUserData);
        });
        String expectedMessage = "Error: unauthorized";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void logout_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        AuthData authData = userService.register(validUserData);
        AuthData returnedAuth = authDAO.getAuth(authData);
        assertNotNull(returnedAuth);

        userService.logout(authData);

        AuthData auth = authDAO.getAuth(authData);
        assertNull(auth);
    }
    @Test
    void logout_negativeCase() throws DataAccessException {
        AuthData invalidAuth = new AuthData("invalidToken", "invalidName");
        Throwable exception = assertThrows(DataAccessException.class, () -> {
            userService.logout(invalidAuth);
        });
        String expectedMessage = "Error: unauthorized";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void clear_positiveCase() throws DataAccessException {
        UserData validUserData = new UserData("validUsername", "validEmail", "validPassword");
        userService.register(validUserData);
        UserData returnedUser = userDAO.getUser(validUserData);
        assertNotNull(returnedUser);
        userService.clear();
        UserData user = userDAO.getUser(validUserData);
        assertNull(user);
    }
    @Test
    void clear_negativeCase() throws DataAccessException {

    }
}