package handlers;

import com.google.gson.Gson;
import requests.*;

public class Serializer<T> {
    final Gson serializer = new Gson();

    public String serialize(T response) {
        return serializer.toJson(response);
    }

    public RegisterRequest deserializeRegister(String json) {
        return serializer.fromJson(json, RegisterRequest.class);
    }

    public LoginRequest deserializeLogin(String json) {
        return serializer.fromJson(json,LoginRequest.class);
    }





    public CreateBody deserializeCreateGame(String json) {
        return serializer.fromJson(json, CreateBody.class);
    }

    public JoinBody deserializeJoinGame(String json) {
        return serializer.fromJson(json, JoinBody.class);
    }


}
