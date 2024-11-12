package client;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import model.UserData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.*;

public class ServerFacade {
    private String url;
    private int statusCode;
    public ServerFacade(String host, String port) {
        url = "http://" + host + ":" + port;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public RegisterResponse register(UserData user) throws ClientException {
        return ClientCommunicator.makeCall("/user", "POST", null, user, RegisterResponse.class);
    }

    public LoginResponse login(UserData user) throws ClientException {
        user = new UserData(user.username(), user.password(), user.email());
        return ClientCommunicator.makeCall("/session", "POST", null, user, LoginResponse.class);
    }

    public LogoutResponse logout(String authToken) throws ClientException {
        return ClientCommunicator.makeCall("/session", "DELETE", authToken, null, LogoutResponse.class);
    }

    public ListGamesResponse listGames(String authToken) throws ClientException {
        return ClientCommunicator.makeCall("/game", "GET", authToken, null, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest req, String authToken) throws ClientException {
        return ClientCommunicator.makeCall("/game", "POST", authToken, req, CreateGameResponse.class);
    }

    public JoinGameResponse joinPlayer(JoinGameRequest req, String var2) throws ClientException {
        return ClientCommunicator.makeCall("/game", "PUT", var2, req, JoinGameResponse.class);
    }

    public ClearAllResponse clear() throws ClientException {
        return ClientCommunicator.makeCall("/db", "DELETE", null, null, ClearAllResponse.class);
    }
}

// create url, get output stream, write body -> server
//ui.Client class w/ menus
//ui.ChessBoard
/*
Client creates a:
2d array with chess pieces Array<ChessPiece>
 */