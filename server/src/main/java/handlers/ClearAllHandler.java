package handlers;

import requestresponses.ClearAllRequest;
import requestresponses.ClearAllResponse;
import service.GameService;
import spark.Request;

public class ClearAllHandler implements Handler {

    public String deserialize(Request json) {
        GameService gameService = new GameService();

        ClearAllRequest req = new ClearAllRequest();
        ClearAllResponse res = gameService.clearAll(req);
        return SERIALIZER.serialize(res);
    }
}
