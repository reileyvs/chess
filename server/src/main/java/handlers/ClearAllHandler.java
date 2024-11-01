package handlers;

import Exceptions.DataAccessException;
import requests.ClearAllRequest;
import responses.ClearAllResponse;
import service.GameService;
import spark.Request;

public class ClearAllHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();

        ClearAllRequest req = new ClearAllRequest();
        ClearAllResponse res = gameService.clearAll(req);
        return SERIALIZER.serialize(res);
    }
}
