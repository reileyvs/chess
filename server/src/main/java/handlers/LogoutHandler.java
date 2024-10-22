package handlers;

import request_responses.LogoutRequest;

public class LogoutHandler implements Handler{

    public LogoutRequest deserialize(String json) {
        return serializer.deserializeLogout(json);
    }
}
