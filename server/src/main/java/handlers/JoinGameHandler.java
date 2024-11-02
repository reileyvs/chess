package handlers;

import exceptions.DataAccessException;
import dataaccess.*;
import requests.*;
import responses.JoinGameResponse;
import service.GameService;
import spark.Request;

public class JoinGameHandler implements Handler {
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    private MySqlGameDAO gameDAO;
    public JoinGameHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
    }

    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService(authDAO, userDAO,gameDAO);
        String authToken = json.headers("Authorization");
        JoinBody body = SERIALIZER.deserializeJoinGame(json.body());
        JoinGameRequest req = new JoinGameRequest(authToken, body.playerColor(), body.gameID());
        JoinGameResponse res = gameService.joinGame(req);
        return SERIALIZER.serialize(res);
    }
}
