package handlers;

import Exceptions.DataAccessException;
import dataaccess.*;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.UserService;
import spark.Request;

public class RegisterHandler implements Handler {
    private MySqlAuthDAO authDAO;
    private MySqlUserDAO userDAO;
    public RegisterHandler(MySqlAuthDAO authDAO, MySqlUserDAO userDAO) {
        this.authDAO=authDAO;
        this.userDAO=userDAO;
    }

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest newRequest = SERIALIZER.deserializeRegister(json.body());
        RegisterResponse res=null;
        res = userService.register(newRequest);
        return SERIALIZER.serialize(res);
    }

}
