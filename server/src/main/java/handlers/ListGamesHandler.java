package handlers;

import dataaccess.DataAccessException;
import model.GameData;
import request_responses.ListGamesRequest;
import request_responses.ListGamesResponse;
import request_responses.LogoutRequest;
import request_responses.LogoutResponse;
import service.GameService;
import service.UserService;
import spark.Request;

import java.util.List;

public class ListGamesHandler implements Handler{

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        ListGamesRequest req = new ListGamesRequest(json.headers("Authorization"));
        List<GameData> gameList = gameService.listGames(req);
        ListGamesResponse res = new ListGamesResponse(gameList);
        return serializer.serialize(res);
    }
}
