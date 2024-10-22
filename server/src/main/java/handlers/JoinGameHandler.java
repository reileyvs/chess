package handlers;

import request_responses.JoinGameRequest;

public class JoinGameHandler implements Handler {

    public JoinGameRequest deserialize(String json) {
        return serializer.deserializeJoinGame(json);
    }
}
