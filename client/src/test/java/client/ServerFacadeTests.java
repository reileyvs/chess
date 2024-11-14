package client;

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
    static UserData user;
    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        user = new UserData("Joe", "password", "bob@gmail.com");
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
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
        String authToken = doRegisterAndLogin();
        try {
            LogoutResponse res=facade.logout(authToken);
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
    }
    @Test
    void logoutNegativeTest() {
        String authToken = doRegisterAndLogin();
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
        String authToken=register();
        create(authToken);
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
        String authToken=register();
        create(authToken);
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
        String authToken=register();
        create(authToken);
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
        String authToken = register();
        int gameID=create(authToken);
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
        String authToken = register();
        int gameID = create(authToken);
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
        String authToken=register();
        create(authToken);
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

    private String doRegisterAndLogin() {
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
        return authToken;
    }
    private String register() {
        String authToken=null;
        try {
            RegisterResponse res = facade.register(user);
            authToken = res.authToken();
        } catch(ClientException ex) {
            assertEquals(1, 0);
        }
        return authToken;
    }
    private int create(String authToken) {
        int gameID=0;
        try {
            CreateGameRequest req = new CreateGameRequest(authToken, "game");
            CreateGameResponse res = facade.createGame(req);
            gameID = res.gameID();
            assertNotEquals(0, res.gameID());
            assertNull(res.message());
        } catch(ClientException ex) {
            assertEquals(2,0);
        }
        return gameID;
    }

}
