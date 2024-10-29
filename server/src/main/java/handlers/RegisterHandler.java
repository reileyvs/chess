package handlers;

import Exceptions.DataAccessException;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.UserService;
import spark.Request;

public class RegisterHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        RegisterRequest newRequest = SERIALIZER.deserializeRegister(json.body());
        RegisterResponse res=null;
        res = userService.register(newRequest);
        return SERIALIZER.serialize(res);
    }

}
