package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import request_responses.CreateGameRequest;
import request_responses.CreateGameResponse;
import request_responses.JoinGameRequest;
import request_responses.ListGamesRequest;

import java.util.List;

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
    public void joinGame(JoinGameRequest request) {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info

    }
    public GameData getGame(int gameID) {
        return GameDAO.getGame(gameID);
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
