package handlers;

import request_responses.CreateGameRequest;

public class CreateGameHandler implements Handler {

    public CreateGameRequest deserialize(String json) {
        return serializer.deserializeCreateGame(json);
    }
}
