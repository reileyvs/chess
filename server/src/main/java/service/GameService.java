package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.SimpleGameData;
import requestresponses.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameService {
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";
    Random random = new Random();
    public List<SimpleGameData> listGames(ListGamesRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken()) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        return GameDAO.listGames();
    }
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken()) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        int gameID =Math.abs(random.nextInt());
        GameData game = new GameData(gameID,null,null,
                request.gameName(), new ChessGame());
        GameDAO.createGame(game);
        if(GameDAO.getGame(game.gameID()) == null) {
            throw new DataAccessException("Game not saved");
        }
        return new CreateGameResponse(gameID);
    }
    public JoinGameResponse joinGame(JoinGameRequest request) throws DataAccessException {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info
        AuthData userAuth = AuthDAO.getAuthByToken(request.authToken());
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        GameData game = GameDAO.getGame(request.gameID());
        GameData updatedGame = null;
        if(game == null) {
            throw new DataAccessException(BAD_REQUEST);
        }
        if(Objects.equals(request.playerColor(), "WHITE")) {
            if(!Objects.equals(game.whiteUsername(), null)) {
                throw new DataAccessException(TAKEN);
            }
            updatedGame = new GameData(game.gameID(), userAuth.username(), game.blackUsername(),
                    game.gameName(), game.game());
        } else if (Objects.equals(request.playerColor(), "BLACK")) {
            if(!Objects.equals(game.blackUsername(), null)) {
                throw new DataAccessException(TAKEN);
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), userAuth.username(),
                    game.gameName(), game.game());
        } else {
            throw new DataAccessException(BAD_REQUEST);
        }
        GameDAO.createGame(updatedGame);
        return new JoinGameResponse();
    }

    public ClearAllResponse clearAll(ClearAllRequest request) {
        AuthDAO.clear();
        GameDAO.clear();
        UserDAO.clear();
        return new ClearAllResponse();
    }


    public GameData getGame(int gameID) {
        return GameDAO.getGame(gameID);
    }
}
