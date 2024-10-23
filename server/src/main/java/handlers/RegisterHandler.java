package handlers;

import dataaccess.DataAccessException;
import request_responses.RegisterRequest;
import request_responses.RegisterResponse;
import service.UserService;
import spark.Request;

import java.util.Objects;

public class RegisterHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        RegisterRequest newRequest = serializer.deserializeRegister(json.body());
        RegisterResponse res=null;
        res = userService.register(newRequest);
        return serializer.serialize(res);
    }

}
