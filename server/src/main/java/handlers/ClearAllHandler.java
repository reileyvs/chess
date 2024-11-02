package handlers;

import Exceptions.DataAccessException;
import dataaccess.*;
import requests.ClearAllRequest;
import responses.ClearAllResponse;
import service.GameService;
import spark.Request;

public class ClearAllHandler implements Handler {
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    private MySqlGameDAO gameDAO;
    public ClearAllHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
    }


    public String deserialize(Request json) throws DataAccessException {
        GameService gameService = new GameService(authDAO, userDAO, gameDAO);

        ClearAllRequest req = new ClearAllRequest();
        ClearAllResponse res = gameService.clearAll(req);
        return SERIALIZER.serialize(res);
    }
}
