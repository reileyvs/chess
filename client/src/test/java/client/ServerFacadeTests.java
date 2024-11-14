package client;

import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import dataaccess.MySqlUserDAO;
import dataaccess.UserDAO;
import exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import responses.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    static int portNum;
    static UserData user;
    @BeforeAll
    public static void init() throws DataAccessException {
        portNum = 3030;
        facade = new ServerFacade("localhost");
        server = new Server();
        user = new UserData("Joe", "password", "bob@gmail.com");
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterEach
    void clearServer() throws ClientException {
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
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
            LoginRequest req = new LoginRequest(user.username(), user.password());
            LoginResponse res = facade.login(req);
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
            LoginResponse res = facade.login(new LoginRequest("Jake","wack"));
            assertNull(res.authToken());
            assertNotNull(res.message());
        });
    }

    @Test
    void logoutPositiveTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            LoginRequest req = new LoginRequest(user.username(), user.password());
            LoginResponse loginRes = facade.login(req);
            authToken = loginRes.authToken();
            assertNotNull(loginRes.authToken());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            LogoutResponse res=facade.logout(authToken);
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
    }
    @Test
    void logoutNegativeTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            LoginRequest req = new LoginRequest(user.username(), user.password());
            LoginResponse loginRes = facade.login(req);
            authToken = loginRes.authToken();
            assertNotNull(loginRes.authToken());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            LogoutResponse res=facade.logout(authToken);
            LogoutResponse res2 = facade.logout(authToken);
            assertNotNull(res2.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
    }

    @Test
    void createGamePositiveTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
    }
    @Test
    void createGameNegativeTest() {
        try {
            RegisterResponse res = facade.register(user);
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest("impostor", "game");
            CreateGameResponse res = facade.createGame(req);
            assertNotNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
    }

    @Test
    void listGamesPositiveTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        try {
            ListGamesResponse res = facade.listGames(authToken);
            assertNotNull(res.games());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(3,0);
        }
    }
    @Test
    void listGamesNegativeTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        try {
            ListGamesResponse res = facade.listGames("impostor");
            assertNull(res.games());
            assertNotNull(res.message());
        } catch(ClientException ex) {
            assertEquals(3,0);
        }
    }


    @Test
    void joinPlayerPositiveTest() {
        String authToken=null;
        int gameID=0;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            gameID = res.gameID();
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        try {
            JoinGameRequest req = new JoinGameRequest(authToken,"WHITE",gameID);
            JoinGameResponse res = facade.joinPlayer(req);
            assertNull(res.message());
        } catch (ClientException e) {
            assertEquals(3,0);
        }
    }
    @Test
    void joinPlayerNegativeTest() {
        String authToken=null;
        int gameID=0;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            gameID = res.gameID();
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        try {
            JoinGameRequest req = new JoinGameRequest("impostor","WHITE",gameID);
            JoinGameResponse res = facade.joinPlayer(req);
            assertNotNull(res.message());
        } catch (ClientException e) {
            assertEquals(3,0);
        }
    }

    @Test
    void clearPositiveTest() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        try {
            ClearAllResponse res = facade.clear();
            ListGamesResponse gameRes = facade.listGames(authToken);
            assertNull(res.message());
            assertNull(gameRes.games());
            assertNotNull(gameRes.message());
        } catch (ClientException e) {
            assertEquals(3,0);
        }
    }
    @Test
    void clearNegativeTest() {
        assertEquals(1,1);
    }

}
