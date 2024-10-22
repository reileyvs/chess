package service;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import request_responses.CreateGameRequest;
import request_responses.JoinGameRequest;

import java.util.Collection;
import java.util.List;

public class GameService {

    public List<GameData> listGames() {
        return GameDAO.listGames();
    }
    public void joinGame(JoinGameRequest request) {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info

    }
    public int createGame(CreateGameRequest request) {

        return 0;
    }
    public GameData getGame(int gameID) {
        return GameDAO.getGame(gameID);
    }
}
