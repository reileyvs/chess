package handlers;

import Exceptions.DataAccessException;
import dataaccess.*;
import requests.CreateBody;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.GameService;
import spark.Request;

public class CreateGameHandler implements Handler {
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    private MySqlGameDAO gameDAO;
    public CreateGameHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
    }
    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService(authDAO, userDAO, gameDAO);
        String authToken = json.headers("Authorization");
        CreateBody body = SERIALIZER.deserializeCreateGame(json.body());
        CreateGameRequest req = new CreateGameRequest(authToken, body.gameName());
        CreateGameResponse res = gameService.createGame(req);
        return SERIALIZER.serialize(res);
    }
}
