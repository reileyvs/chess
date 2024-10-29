package handlers;

import Exceptions.DataAccessException;
import requests.*;
import responses.JoinGameResponse;
import service.GameService;
import spark.Request;

public class JoinGameHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService();
        String authToken = json.headers("Authorization");
        JoinBody body = SERIALIZER.deserializeJoinGame(json.body());
        JoinGameRequest req = new JoinGameRequest(authToken, body.playerColor(), body.gameID());
        JoinGameResponse res = gameService.joinGame(req);
        return SERIALIZER.serialize(res);
    }
}
