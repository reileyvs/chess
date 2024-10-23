package handlers;

import dataaccess.DataAccessException;
import requestresponses.LoginRequest;
import requestresponses.LoginResponse;
import service.UserService;
import spark.Request;

public class LoginHandler implements Handler {

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        LoginRequest newRequest = SERIALIZER.deserializeLogin(json.body());
        LoginResponse res = userService.login(newRequest);
        return SERIALIZER.serialize(res);
    }
}
