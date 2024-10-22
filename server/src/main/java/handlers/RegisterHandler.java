package handlers;

import request_responses.RegisterRequest;

public class RegisterHandler implements Handler {

    public RegisterRequest deserialize(String json) {
        return serializer.deserializeRegister(json);
    }

}
