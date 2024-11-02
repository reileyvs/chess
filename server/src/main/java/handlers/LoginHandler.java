package handlers;

import exceptions.DataAccessException;
import dataaccess.*;
import requests.LoginRequest;
import responses.LoginResponse;
import service.UserService;
import spark.Request;

public class LoginHandler implements Handler {
    private final MySqlAuthDAO authDAO;
    private final MySqlUserDAO userDAO;
    public LoginHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
    }
    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService(authDAO, userDAO);
        LoginRequest newRequest = SERIALIZER.deserializeLogin(json.body());
        LoginResponse res = userService.login(newRequest);
        return SERIALIZER.serialize(res);
    }
}
