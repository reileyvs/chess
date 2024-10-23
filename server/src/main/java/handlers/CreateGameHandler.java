package handlers;

import dataaccess.DataAccessException;
import request_responses.CreateGameRequest;
import request_responses.CreateGameResponse;
import service.GameService;
import spark.Request;

public class CreateGameHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        String authToken = json.headers("Authorization");

        CreateGameRequest req = new CreateGameRequest(authToken, serializer.deserializeCreateGame(json.body()).name());
        CreateGameResponse res = gameService.createGame(req);
        return serializer.serialize(res);
    }
}
