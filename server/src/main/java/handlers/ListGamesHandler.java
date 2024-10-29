package handlers;

import Exceptions.DataAccessException;
import model.SimpleGameData;
import requests.ListGamesRequest;
import responses.ListGamesResponse;
import service.GameService;
import spark.Request;

import java.util.List;

public class ListGamesHandler implements Handler{

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        ListGamesRequest req = new ListGamesRequest(json.headers("Authorization"));
        List<SimpleGameData> gameList = gameService.listGames(req);
        ListGamesResponse res = new ListGamesResponse(gameList);
        return SERIALIZER.serialize(res);
    }
}
