package handlers;

import request_responses.LoginRequest;

public class LoginHandler implements Handler {

    public LoginRequest deserialize(String json) {
        return serializer.deserializeLogin(json);
    }
}
