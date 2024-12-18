package dataaccess;

import exceptions.DataAccessException;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertDoesNotThrow(()-> {
            assertFalse(dao.getGames().isEmpty());
        });
    }

    @Test
    void createGameTestPositive() {
        try {
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
            ChessGame chessGame = new ChessGame();
            chessGame.setTeamTurn(teamColor);
            dao.addGame(new GameData(4,"Job","Jack","name",chessGame));
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        assertDoesNotThrow(() -> {
            GameData game = dao.getGame(4);
            assertEquals(ChessGame.TeamColor.BLACK, game.game().getTeamTurn());
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
