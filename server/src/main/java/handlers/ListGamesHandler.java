package handlers;

import exceptions.DataAccessException;
import dataaccess.*;
import model.SimpleGameData;
import requests.ListGamesRequest;
import responses.ListGamesResponse;
import service.GameService;
import spark.Request;

import java.util.List;

public class ListGamesHandler implements Handler{
    private final MySqlAuthDAO authDAO;
    private final MySqlUserDAO userDAO;
    private final MySqlGameDAO gameDAO;
    public ListGamesHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
    }
    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService(authDAO, userDAO, gameDAO);
        ListGamesRequest req = new ListGamesRequest(json.headers("Authorization"));
        List<SimpleGameData> gameList = gameService.listGames(req);
        ListGamesResponse res = new ListGamesResponse(gameList, null);
        return SERIALIZER.serialize(res);
    }
}
