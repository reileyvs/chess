package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
    @BeforeEach
    void setup() {
        userService = new UserService();
        request = new RegisterRequest("John","password", "a@byu.org");
    }
    @AfterEach
    void takeDown() {
        UserDAO.clear();
        AuthDAO.clear();
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
    void logoutPositive() {
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