package handlers;

import dataaccess.DataAccessException;
import requests.CreateBody;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.GameService;
import spark.Request;

public class CreateGameHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        String authToken = json.headers("Authorization");
        CreateBody body = SERIALIZER.deserializeCreateGame(json.body());
        CreateGameRequest req = new CreateGameRequest(authToken, body.gameName());
        CreateGameResponse res = gameService.createGame(req);
        return SERIALIZER.serialize(res);
    }
}
