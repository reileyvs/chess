package handlers;

import dataaccess.DataAccessException;
import request_responses.CreateBody;
import request_responses.CreateGameRequest;
import request_responses.CreateGameResponse;
import service.GameService;
import spark.Request;

public class CreateGameHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        String authToken = json.headers("Authorization");
        CreateBody body = serializer.deserializeCreateGame(json.body());
        CreateGameRequest req = new CreateGameRequest(authToken, body.gameName());
        CreateGameResponse res = gameService.createGame(req);
        return serializer.serialize(res);
    }
}
