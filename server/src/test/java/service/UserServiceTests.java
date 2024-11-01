package service;

import Exceptions.RecordException;
import dataaccess.AuthDAO;
import Exceptions.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {
    UserService userService;
    RegisterRequest request;
    AuthDAO authDAO;
    @BeforeEach
    void setup() throws DataAccessException {
        userService = new UserService();
        request = new RegisterRequest("John","password", "a@byu.org");
        authDAO = new AuthDAO();
        DatabaseManager.createDatabase();
    }
    @AfterEach
    void takeDown() {
        try {
            UserDAO.clear();
            authDAO.clear();
        } catch(DataAccessException ex) {
            assertEquals(1,2);
        }
    }
    @Test
    void registerPositive() {
        try {
            userService.register(request);
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        assertNotNull(userService.getUser("John"));
    }
    @Test
    void registerNegative() {
        RegisterRequest request = new RegisterRequest("John", "password", "a@byu.org");
        RegisterRequest requestDup = new RegisterRequest("John", "password2", "b@byu.org");
        try {
            userService.register(request);
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        assertThrows(DataAccessException.class,() -> {
            userService.register(requestDup);
        });
    }

    @Test
    void loginPositive() {
        UserData tester = new UserData("John", "password","a@byu.org");
        LoginRequest loginRequest = new LoginRequest(tester.username(), tester.password());
        try {
            userService.register(request);
            userService.login(loginRequest);
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        UserData user = userService.getUser(tester.username());
        assertNotNull(user);
        assertEquals(user.username(), tester.username());
        assertEquals(user.password(), tester.password());
    }
    @Test
    void loginNegative() {
        UserData tester = new UserData("Jon", "password","a@byu.org");
        LoginRequest loginRequest = new LoginRequest(tester.username(), tester.password());
        assertThrows(DataAccessException.class, () -> {
            userService.register(request);
            userService.login(loginRequest);
        });
    }

    @Test
    void logoutPositive() throws RecordException {
        UserData tester = new UserData(request.username(),request.password(),request.email());
        try {
            RegisterResponse response = userService.register(request);
            assertNotNull(userService.getAuth(tester.username()));
            LogoutRequest logoutRequest = new LogoutRequest(response.authToken());
            userService.logout(logoutRequest);
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        AuthData userAuth = userService.getAuth(tester.username());
        assertNull(userAuth);
    }
    @Test
    void logoutNegative() {
        UserData tester = new UserData(request.username(),request.password(),request.email());
        String auth = "yeehaw";
        LogoutRequest logoutRequest = new LogoutRequest(auth);

        assertThrows(DataAccessException.class, () -> {
            userService.logout(logoutRequest);
        });
    }
}