package handlers;

import dataaccess.DataAccessException;
import request_responses.LoginRequest;
import request_responses.LoginResponse;
import request_responses.LogoutRequest;
import request_responses.LogoutResponse;
import service.UserService;
import spark.Request;

public class LogoutHandler implements Handler{

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        LogoutRequest req = new LogoutRequest(json.headers("Authorization"));
        LogoutResponse res = userService.logout(req);
        return serializer.serialize(res);
    }
}
