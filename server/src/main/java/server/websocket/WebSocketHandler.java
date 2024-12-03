package server.websocket;

import com.google.gson.*;
import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import dataaccess.MySqlUserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Type;

@WebSocket
public class WebSocketHandler extends Endpoint {
    private int port = 0;
    private MySqlAuthDAO auth;
    private MySqlUserDAO user;
    private MySqlGameDAO game;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(int port, MySqlAuthDAO auth, MySqlUserDAO user, MySqlGameDAO game) {
        this.port = port;
        this.auth = auth;
        this.user = user;
        this.game = game;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        GsonBuilder builder = new GsonBuilder();
        System.out.println("Zoo-wee mama!");
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
        System.out.println("Let's connect!");
        connections.add(connect.getAuthToken(), session);
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "A player connected");
        try {
            connections.broadcast(connect.getAuthToken(), msg);
        } catch (IOException ex) {
            System.out.println("There was an error when broadcasting");
        }
    }

    private void makeMove(Session session, MakeMove makeMove) {
        System.out.println("Let's move!");
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "A player moved");
    }

    private void leave(Session session, Leave leave) {
        System.out.println("Let's get outta here!");
    }

    private void resign(Session session, Resign resign) {
        System.out.println("Let's resign!");
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "A player resigned");
        try {
            connections.broadcast(resign.getAuthToken(), msg);
        } catch (IOException ex) {
            System.out.println("There was an error when broadcasting");
        }
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
