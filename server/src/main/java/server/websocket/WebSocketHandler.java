package server.websocket;

import com.google.gson.*;
import dataaccess.*;
import exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import requests.ListGamesRequest;
import server.Server;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

@WebSocket
public class WebSocketHandler extends Endpoint {
    int port;
    private GameService gameService;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(int port, MySqlAuthDAO auth, MySqlUserDAO user, MySqlGameDAO game) {
        this.port = port;
        gameService = new GameService(auth, user, game);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new CommandDeserializer());
        Gson gson = builder.create();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(session, (Connect)command);
            case MAKE_MOVE -> makeMove(session, (MakeMove)command);
            case LEAVE -> leave(session, (Leave)command);
            case RESIGN -> resign(session, (Resign)command);
        }
    }

    private void connect(Session session, Connect connect) {
        connections.add(connect.getAuthToken(), session);
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "A player connected");
        try {
            connections.announce(connect.getAuthToken(), msg);
        } catch (IOException ex) {
            System.out.println("There was an error when broadcasting");
        }
    }

    private void makeMove(Session session, MakeMove makeMove) {
        ServerMessage msg = gameService.makeMove(makeMove);
        try {
            connections.announce(makeMove.getAuthToken(), msg);
        } catch (IOException ex) {
            System.out.println("There was an error notifying the other players");
        }
    }

    private void leave(Session session, Leave leave) {
        GameData game;
        try {
            game = gameService.gameDAO.getGame(leave.getGameID());
            if (game.whiteUsername().equals(leave.getUsername())) {
                game = new GameData(game.gameID(), null, game.blackUsername(),
                        game.gameName(), game.game());
            } else if (game.blackUsername().equals(leave.getUsername())) {
                game = new GameData(game.gameID(), game.whiteUsername(), null,
                        game.gameName(), game.game());
            }
            gameService.gameDAO.addGame(game);
        } catch (DataAccessException ex) {
            System.out.println("There was an error updating the game");
        }
        connections.remove(leave.getAuthToken());
        System.out.println("Let's get outta here!");
    }

    private void resign(Session session, Resign resign) {
        var req = new ListGamesRequest(resign.getAuthToken());
        String winner = null;
        try {
            var games = gameService.listGames(req);
            for (var game : games) {
                if (game.gameID() == resign.getGameID()) {
                    if(Objects.equals(game.whiteUsername(), resign.getUsername())) {
                        winner = game.blackUsername();
                    } else if (Objects.equals(game.blackUsername(), resign.getUsername())) {
                        winner = game.whiteUsername();
                    }
                }
            }
        } catch (DataAccessException ex) {
            System.out.println("There was an error getting games");
        }
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                resign.getUsername() + " resigned!\n" + winner + " has won the game!!!", true);
        try {
            connections.announce(resign.getAuthToken(), msg);
        } catch (IOException ex) {
            System.out.println("There was an error when announcing");
        }
    }

    @OnWebSocketError
    public void error(Session session, Throwable msg) {
        System.out.println(msg.getMessage());
    }

    public static class CommandDeserializer implements JsonDeserializer<UserGameCommand> {
        @Override
        public UserGameCommand deserialize(
                JsonElement jsonElement,
                Type type,
                JsonDeserializationContext context
                ) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

            return switch(commandType) {
                case CONNECT -> context.deserialize(jsonElement, Connect.class);
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMove.class);
                case RESIGN -> context.deserialize(jsonElement, Resign.class);
                case LEAVE -> context.deserialize(jsonElement, Leave.class);
            };
        }
    }
}
