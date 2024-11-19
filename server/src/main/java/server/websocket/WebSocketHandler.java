package server.websocket;

import com.google.gson.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;

import java.io.IOException;
import java.lang.reflect.Type;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        //Todo: Make type adapter here
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new CommandDeserializer());
        Gson gson = builder.create();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
        switch(command.getCommandType()) {

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
