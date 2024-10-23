package handlers;

import request_responses.ClearAllRequest;
import request_responses.ClearAllResponse;
import request_responses.CreateGameRequest;
import request_responses.CreateGameResponse;
import service.GameService;
import spark.Request;

public class ClearAllHandler implements Handler {

    public String deserialize(Request json) {
        GameService gameService = new GameService();

        ClearAllRequest req = new ClearAllRequest();
        ClearAllResponse res = gameService.clearAll(req);
        return serializer.serialize(res);
    }
}
