package client;

import exceptions.DataAccessException;
import model.UserData;

import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import responses.*;
//import server.Server;

public class ServerFacade {
    private ClientCommunicator client;
    //private Server server;
    private int statusCode;
    public ServerFacade(String url) throws DataAccessException {
        client = new ClientCommunicator(url);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public RegisterResponse register(UserData user) throws ClientException {
        return client.makeCall("/user", "POST", null, user, RegisterResponse.class);
    }

    public LoginResponse login(LoginRequest user) throws ClientException {
        return client.makeCall("/session", "POST", null, user, LoginResponse.class);
    }

    public LogoutResponse logout(String authToken) throws ClientException {
        return client.makeCall("/session", "DELETE", authToken, null, LogoutResponse.class);
    }

    public ListGamesResponse listGames(String authToken) throws ClientException {
        return client.makeCall("/game", "GET", authToken, null, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest req) throws ClientException {
        return client.makeCall("/game", "POST", req.authToken(), req, CreateGameResponse.class);
    }

    public JoinGameResponse joinPlayer(JoinGameRequest req) throws ClientException {
        return client.makeCall("/game", "PUT", req.authToken(), req, JoinGameResponse.class);
    }

    public ClearAllResponse clear() throws ClientException {
        return client.makeCall("/db", "DELETE", null, null, ClearAllResponse.class);
    }
}

// create url, get output stream, write body -> server
//ui.Client class w/ menus
//ui.ChessBoard
/*
Client creates a:
2d array with chess pieces Array<ChessPiece>
 */