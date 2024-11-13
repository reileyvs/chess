package service;

import chess.ChessGame;
import dataaccess.*;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.SimpleGameData;
import requests.*;
import responses.ClearAllResponse;
import responses.CreateGameResponse;
import responses.JoinGameResponse;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameService {
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";
    final Random random = new Random();
    public final MySqlAuthDAO dao;
    public final MySqlUserDAO use;
    public final MySqlGameDAO gameDAO;
    public GameService(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.dao = authDAO;
        this.use = userDAO;
        this.gameDAO = gameDAO;
    }

    public List<SimpleGameData> listGames(ListGamesRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken(), dao) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        return GameDAO.listGames(gameDAO);
    }
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken(), dao) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        int gameID =Math.abs(random.nextInt());
        GameData game = new GameData(gameID,null,null,
                request.gameName(), new ChessGame());
        GameDAO.createGame(game,gameDAO);
        if(GameDAO.getGame(game.gameID(), gameDAO) == null) {
            throw new DataAccessException("Game not saved");
        }
        return new CreateGameResponse(gameID, null);
    }
    public JoinGameResponse joinGame(JoinGameRequest request) throws DataAccessException {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info
        AuthData userAuth = AuthDAO.getAuthByToken(request.authToken(), dao);
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        GameData game = GameDAO.getGame(request.gameID(), gameDAO);
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
        GameDAO.createGame(updatedGame, gameDAO);
        return new JoinGameResponse(null);
    }

    public ClearAllResponse clearAll(ClearAllRequest request) throws DataAccessException {
        AuthDAO.clear(dao);
        GameDAO.clear(gameDAO);
        UserDAO.clear(use);
        return new ClearAllResponse(null);
    }



}
