package handlers;

import Exceptions.DataAccessException;
import dataaccess.*;
import requests.LogoutRequest;
import responses.LogoutResponse;
import service.UserService;
import spark.Request;

public class LogoutHandler implements Handler{
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    public LogoutHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
    }

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService(authDAO, userDAO);
        LogoutRequest req = new LogoutRequest(json.headers("Authorization"));
        LogoutResponse res = userService.logout(req);
        return SERIALIZER.serialize(res);
    }
}
