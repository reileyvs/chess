package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceTests {
        GameService gameService;
        List<GameData> testGames = Arrays.asList(new GameData[]{
                (new GameData(1, "", "", "", new ChessGame())),
                new GameData(2, "", "", "",new ChessGame()),
                new GameData(3,"","","",new ChessGame())});

    @BeforeEach
        void setup() {
                gameService = new GameService();

                GameDAO.createGame(testGames.get(0));
                GameDAO.createGame(testGames.get(1));
                GameDAO.createGame(testGames.get(2));
        }

        @Test
        void listGamesTestPositive() {
                List<GameData> games = gameService.listGames();
                for (int i = 0; i < testGames.size(); i++) {
                        assertEquals(games.get(i).gameID(),testGames.get(i).gameID());
                }
        }
        @Test
        void listGamesTestNegative() {

        }

        @Test
        void createGameTestPositive() {

        }
        @Test
        void createGameTestNegative() {

        }

        @Test
        void joinGamePositiveTest() {

        }
        @Test
        void joinGameNegativeTest() {

        }

        @Test
        void clearAllTestPositive() {

        }

}
