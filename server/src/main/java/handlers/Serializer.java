package handlers;

import com.google.gson.Gson;
import request_responses.*;

public class Serializer<T> {
    Gson serializer = new Gson();

    public String serialize(T response) {
        return serializer.toJson(response);
    }
    public RegisterRequest deserializeRegister(String json) {
        return serializer.fromJson(json, RegisterRequest.class);
    }

    public LoginRequest deserializeLogin(String json) {
        return serializer.fromJson(json,LoginRequest.class);
    }

    public LogoutRequest deserializeLogout(String json) {
        return serializer.fromJson(json,LogoutRequest.class);
    }

    public ListGamesRequest deserializeListGames(String json) {
        return serializer.fromJson(json,ListGamesRequest.class);
    }

    public CreateGameRequest deserializeCreateGame(String json) {
        return serializer.fromJson(json,CreateGameRequest.class);
    }

    public JoinGameRequest deserializeJoinGame(String json) {
        return serializer.fromJson(json,JoinGameRequest.class);
    }

    public ClearAllRequest deserializeClearAll(String json) {
        return serializer.fromJson(json,ClearAllRequest.class);
    }
}
