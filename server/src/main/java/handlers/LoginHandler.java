package handlers;

import dataaccess.DataAccessException;
import request_responses.LoginRequest;
import request_responses.LoginResponse;
import service.UserService;
import spark.Request;

import java.util.Objects;

public class LoginHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        LoginRequest newRequest = serializer.deserializeLogin(json.body());
        LoginResponse res = userService.login(newRequest);
        return serializer.serialize(res);
    }
}
