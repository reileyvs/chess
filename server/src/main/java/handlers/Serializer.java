package handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import request_responses.*;

import java.util.List;

public class Serializer<T> {
    Gson serializer = new Gson();

    public String serialize(T response) {
        return serializer.toJson(response);
    }
    public String serializeStringArray(ListGamesResponse response) {
        return serializer.toJson(response, new TypeToken<List<String>[]>() {}.getType());
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

    public CreateBody deserializeCreateGame(String json) {
        return serializer.fromJson(json, CreateBody.class);
    }

    public JoinBody deserializeJoinGame(String json) {
        return serializer.fromJson(json, JoinBody.class);
    }

    public CreateBody deserializeClearAll(String json) {
        return serializer.fromJson(json, CreateBody.class);
    }
}
