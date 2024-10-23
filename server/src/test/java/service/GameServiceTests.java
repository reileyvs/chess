package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_responses.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTests {
        GameService gameService;
        UserService userService;
        RegisterRequest user = new RegisterRequest("John","password","a@byu.org");
        List<GameData> testGames = Arrays.asList(new GameData[]{
                (new GameData(1, "", "", "", new ChessGame())),
                new GameData(2, "", "Jane", "",new ChessGame()),
                new GameData(3,"John","","",new ChessGame())});
        RegisterResponse response;
    GameServiceTests() throws DataAccessException {
    }

    @BeforeEach
        void setup() throws DataAccessException {
                gameService = new GameService();
                userService = new UserService();
                GameDAO.createGame(testGames.get(0));
                GameDAO.createGame(testGames.get(1));
                GameDAO.createGame(testGames.get(2));
                gameService.setGameID(3);
                response = userService.register(user);

        }

        @Test
        void listGamesTestPositive() {
            try {
                    List<GameData> games=gameService.listGames(new ListGamesRequest(response.authToken()));
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
                    List<GameData> games=gameService.listGames(new ListGamesRequest("authTokenWrong"));
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
            assertEquals(4, gameID.gameID());
        }
        @Test
        void createGameTestNegative() {
            assertThrows(DataAccessException.class, () -> {
                CreateGameRequest req = new CreateGameRequest("WrongAuthToken", "newGame");
                CreateGameResponse gameID = gameService.createGame(req);
            });
        }

        @Test
        void joinGamePositiveTest() {
            int gameID = 1;
            JoinGameRequest req = new JoinGameRequest(response.authToken(), "WHITE", gameID);
            assertDoesNotThrow(() -> {
                gameService.joinGame(req);
            });
            assertNotEquals("", GameDAO.getGame(gameID).whiteUsername());
            assertEquals(GameDAO.getGame(gameID).whiteUsername(), response.username());
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
        void clearAllTestPositive() {
            assertFalse(GameDAO.gameDb.getGames().isEmpty());
            assertFalse(UserDAO.userDb.getUsers().isEmpty());
            assertFalse(AuthDAO.authDb.getAllAuthData().isEmpty());

            ClearAllRequest req = new ClearAllRequest();
            gameService.clearAll(req);

            assertTrue(GameDAO.gameDb.getGames().isEmpty());
            assertTrue(UserDAO.userDb.getUsers().isEmpty());
            assertTrue(AuthDAO.authDb.getAllAuthData().isEmpty());
        }

}
