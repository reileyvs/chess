package handlers;

import com.google.gson.Gson;
import request_responses.*;

public class Serializer<T> {
    Gson serializer = new Gson();

    public String serializeRegister(T response) {
        return serializer.toJson(response);
    }
    public RegisterRequest deserializeRegister(String json) {
        return serializer.fromJson(json, RegisterRequest.class);
    }

    public String serializeLogin(T response) {
        return serializer.toJson(response);
    }
    public LoginRequest deserializeLogin(String json) {
        return serializer.fromJson(json,LoginRequest.class);
    }

    public String serializeLogout(T response) {
        return serializer.toJson(response);
    }
    public LogoutRequest deserializeLogout(String json) {
        return serializer.fromJson(json,LogoutRequest.class);
    }

    public String serializeListGames(T response) {
        return serializer.toJson(response);
    }
    public ListGamesResponse deserializeListGames(String json) {
        return serializer.fromJson(json,ListGamesResponse.class);
    }

    public String serializeCreateGame(T response) {
        return serializer.toJson(response);
    }
    public CreateGameRequest deserializeCreateGame(String json) {
        return serializer.fromJson(json,CreateGameRequest.class);
    }

    public String serializeJoinGame(T response) {
        return serializer.toJson(response);
    }
    public JoinGameRequest deserializeJoinGame(String json) {
        return serializer.fromJson(json,JoinGameRequest.class);
    }

    public String serializeClearAll(T response) {
        return serializer.toJson(response);
    }
    public ClearAllRequest deserializeClearAll(String json) {
        return serializer.fromJson(json,ClearAllRequest.class);
    }
}
