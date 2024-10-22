package handlers;

import request_responses.ListGamesRequest;

public class ListGamesHandler implements Handler{

    public ListGamesRequest deserialize(String json) {
        return serializer.deserializeListGames(json);
    }
}
