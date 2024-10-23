package handlers;

import dataaccess.DataAccessException;
import requestresponses.LogoutRequest;
import requestresponses.LogoutResponse;
import service.UserService;
import spark.Request;

public class LogoutHandler implements Handler{

    public String deserialize(Request json) throws DataAccessException {
        UserService userService = new UserService();
        LogoutRequest req = new LogoutRequest(json.headers("Authorization"));
        LogoutResponse res = userService.logout(req);
        return SERIALIZER.serialize(res);
    }
}
