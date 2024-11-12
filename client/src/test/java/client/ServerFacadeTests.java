package client;

import model.UserData;
import org.junit.jupiter.api.*;
import responses.LoginResponse;
import responses.RegisterResponse;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    static int portNum;
    static UserData user;

    @BeforeAll
    public static void init() {
        portNum = 3030;
        facade = new ServerFacade("localhost",Integer.toString(portNum));
        server = new Server();
        user = new UserData("Joe", "password", "bob@gmail.com");
        var port = server.run(portNum);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ClientException {
        facade.clear();
        server.stop();
    }


    @Test
    void registerPositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResponse res = facade.register(user);
            assertNotNull(res.authToken());
            assertNull(res.message());
        });
    }
    @Test
    void registerNegativeTest() {
        assertDoesNotThrow(()-> {
            RegisterResponse res = facade.register(user);
        });
        assertDoesNotThrow(() -> {
            RegisterResponse res = facade.register(user);
            assertNotNull(res.message());
        });
    }

    @Test
    void loginPositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResponse res = facade.register(user);
        });
        assertDoesNotThrow(() -> {
            LoginResponse res = facade.login(user);
            assertNotNull(res.authToken());
            assertNull(res.message());
        });
    }
    @Test
    void loginNegativeTest() {
        assertDoesNotThrow(() -> {
            RegisterResponse res = facade.register(user);
        });
        assertDoesNotThrow(() -> {
            LoginResponse res = facade.login(new UserData("Jake","wack", "notamail"));
            assertNull(res.authToken());
            assertNotNull(res.message());
        });
    }

    @Test
    void logoutPositiveTest() {

    }
    @Test
    void logoutNegativeTest() {

    }

    @Test
    void listGamesPositiveTest() {

    }
    @Test
    void listGamesNegativeTest() {

    }

    @Test
    void createGamePositiveTest() {

    }
    @Test
    void createGameNegativeTest() {

    }

    @Test
    void joinPlayerPositiveTest() {

    }
    @Test
    void joinPlayerNegativeTest() {

    }

    @Test
    void clearPositiveTest() {

    }
    @Test
    void clearNegativeTest() {

    }

}
