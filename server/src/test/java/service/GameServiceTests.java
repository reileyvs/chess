package service;

import chess.ChessGame;
import dataaccess.*;
import exceptions.DataAccessException;
import model.GameData;
import model.SimpleGameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.*;
import responses.CreateGameResponse;
import responses.RegisterResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTests {
        GameService gameService;
        UserService userService;
        MySqlAuthDAO auth;
        MySqlUserDAO use;
        MySqlGameDAO dao;
        final RegisterRequest user = new RegisterRequest("John","password","a@byu.org");
        final List<GameData> testGames = Arrays.asList(new GameData[]{
                (new GameData(1, null, null, "one", new ChessGame())),
                new GameData(2, null, "Jane", "two",new ChessGame()),
                new GameData(3,"John",null,"three",new ChessGame())});
        RegisterResponse response;

    @BeforeEach
    void setup() throws DataAccessException {
            DatabaseManager.createDatabase();
            use = new MySqlUserDAO();
            auth = new MySqlAuthDAO();
            dao = new MySqlGameDAO();
            gameService = new GameService(auth,use,dao);
            userService = new UserService(auth,use);
            GameDAO.createGame(testGames.get(0), dao);
            GameDAO.createGame(testGames.get(1), dao);
            GameDAO.createGame(testGames.get(2), dao);
            response = userService.register(user);
    }
    @AfterEach
    void takeDown() {
        try {
            GameDAO.clear(dao);
            UserDAO.clear(use);
            AuthDAO.clear(auth);
        } catch(DataAccessException ex) {
            assertEquals(1,2);
        }
    }

    @Test
    void listGamesTestPositive() {
        try {
            List<SimpleGameData> games=(ArrayList<SimpleGameData>) gameService.listGames(new ListGamesRequest(response.authToken()));
                for (int i=0; i < testGames.size(); i++) {
                        assertEquals(games.get(i).gameID(), testGames.get(i).gameID());
                }
        } catch(DataAccessException ex) {
                System.out.println(ex.getMessage());
        }
    }
    @Test
    void listGamesTestNegative() {
        assertThrows(DataAccessException.class, () -> {
            List<SimpleGameData> games=gameService.listGames(new ListGamesRequest("authTokenWrong"));
        });
    }

    @Test
    void createGameTestPositive() {
        CreateGameResponse gameID=null;
        try {
            CreateGameRequest req = new CreateGameRequest(response.authToken(), "newGame");
            gameID = gameService.createGame(req);
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        assert gameID != null;
        assertNotEquals(3, gameID.gameID());
    }
    @Test
    void createGameTestNegative() {
        assertThrows(DataAccessException.class, () -> {
            CreateGameRequest req = new CreateGameRequest("WrongAuthToken", "newGame");
            CreateGameResponse gameID = gameService.createGame(req);
        });
    }

    @Test
    void joinGamePositiveTest() throws DataAccessException {
        int gameID = 1;
        JoinGameRequest req = new JoinGameRequest(response.authToken(), "WHITE", gameID);
        assertDoesNotThrow(() -> {
            gameService.joinGame(req);
        });
        assertNotEquals("", GameDAO.getGame(gameID,dao).whiteUsername());
        assertEquals(response.username(), GameDAO.getGame(gameID, dao).whiteUsername());
    }
    @Test
    void joinGameNegativeTest() {
        int gameID = 3;
        JoinGameRequest req = new JoinGameRequest(response.authToken(), "WHITE", gameID);
        assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(req);
        }, "already taken");

        gameID = 1;
        JoinGameRequest req2 = new JoinGameRequest("WrongAuthToken", "WHITE", gameID);
        assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(req2);
        }, "Auth problem");
    }

    @Test
    void clearAllTestPositive() throws DataAccessException {
        assertFalse(dao.getGames().isEmpty());
        assertFalse(use.getUsers().isEmpty());
        assertFalse(auth.getAllAuthData().isEmpty());

        ClearAllRequest req = new ClearAllRequest();
        gameService.clearAll(req);

        assertTrue(dao.getGames().isEmpty());
        assertTrue(use.getUsers().isEmpty());
        assertTrue(auth.getAllAuthData().isEmpty());
    }

}
