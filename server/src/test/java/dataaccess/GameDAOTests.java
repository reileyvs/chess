package dataaccess;

import Exceptions.DataAccessException;
import chess.ChessGame;
import model.GameData;
import model.SimpleGameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.*;
import responses.CreateGameResponse;
import responses.RegisterResponse;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameDAOTests {
    MySqlGameDAO dao;
    final List<GameData> testGames = Arrays.asList(new GameData[]{
            (new GameData(1, null, null, "one", new ChessGame())),
            new GameData(2, null, "Jane", "two",new ChessGame()),
            new GameData(3,"John",null,"three",new ChessGame())});

    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        dao = new MySqlGameDAO();
        GameDAO.createGame(testGames.get(0), dao);
        GameDAO.createGame(testGames.get(1), dao);
        GameDAO.createGame(testGames.get(2), dao);
    }
    @AfterEach
    void takeDown() {
        try {
            GameDAO.clear(dao);
        } catch(DataAccessException ex) {
            assertEquals(1,2);
        }
    }

    @Test
    void listGamesTestPositive() {
        try {
            List<GameData> games = dao.getGames();
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
            List<GameData> games=dao.getGames();
        });
    }

    @Test
    void createGameTestPositive() {
        try {
            dao.addGame(new GameData(4,"Job","Jack","name",new ChessGame()));
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        assertDoesNotThrow(() -> {
            assertNotEquals(3, dao.getGame(4).gameID());
        });
    }
    @Test
    void createGameTestNegative() {
        assertThrows(DataAccessException.class, () -> {
            dao.addGame(new GameData(1,"","","",new ChessGame()));
        });
    }

    @Test
    void clearTestPositive() throws DataAccessException {
        assertFalse(dao.getGames().isEmpty());

        dao.clearGames();

        assertTrue(dao.getGames().isEmpty());
    }
}
