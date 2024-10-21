package handlers;

import request_responses.RegisterRequest;
import request_responses.RegisterResponse;

public class RegisterHandler implements Handler {
    public String serialize(RegisterResponse response) {
        return serializer.serializeRegister(response);
    }
    public RegisterRequest deserialize(String json) {
        return serializer.deserializeRegister(json);
    }

}
