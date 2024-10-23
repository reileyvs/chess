package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import request_responses.*;

import java.util.List;
import java.util.Objects;

public class GameService {
    private int gameID = 0;
    public List<GameData> listGames(ListGamesRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return GameDAO.listGames();
    }
    public CreateGameResponse createGame(@NotNull CreateGameRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        gameID += 1;
        GameData game = new GameData(gameID,"","",
                request.gameName(), new ChessGame());
        GameDAO.createGame(game);
        if(GameDAO.getGame(gameID) == null) {
            throw new DataAccessException("Game not saved");
        }
        return new CreateGameResponse(gameID);
    }
    public JoinGameResponse joinGame(JoinGameRequest request) throws DataAccessException {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info
        AuthData userAuth = AuthDAO.getAuthByToken(request.authToken());
        if(userAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        GameData game = GameDAO.getGame(request.GameID());
        GameData updatedGame = null;
        if(game == null) {
            throw new DataAccessException("No game exists");
        }
        if(Objects.equals(request.playerColor(), "WHITE")) {
            if(!Objects.equals(game.whiteUsername(), "")) {
                throw new DataAccessException("already taken");
            }
            updatedGame = new GameData(game.gameID(), userAuth.username(), game.blackUsername(),
                    game.gameName(), game.game());
        } else if (Objects.equals(request.playerColor(), "BLACK")) {
            if(!Objects.equals(game.blackUsername(), "")) {
                throw new DataAccessException("already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), userAuth.username(),
                    game.gameName(), game.game());
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
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
