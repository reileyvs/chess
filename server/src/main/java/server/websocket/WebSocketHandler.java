package server.websocket;

import com.google.gson.*;
import dataaccess.*;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import requests.ListGamesRequest;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private GameService gameService;
    private UserService userService;
    private Gson gson;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(MySqlAuthDAO auth, MySqlUserDAO user, MySqlGameDAO game) {
        userService = new UserService(auth, user);
        gameService = new GameService(auth, user, game);
        gson = new Gson();
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
        if (checkValidToken(session, connect.getAuthToken(), connect)) {
            return;
        }
        connections.add(connect.getAuthToken(), connect.getGameID(), session);
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                connect.getUsername() + " joined the game");
        ListGamesRequest req = new ListGamesRequest(connect.getAuthToken());
        GameData game = null;
        try {
            List<GameData> res = gameService.listGames(req);
            for (var games : res) {
                if (games.gameID() == connect.getGameID()) {
                    game = games;
                    break;
                }
            }
            if (game == null) {
                throw new DataAccessException("");
            }
        } catch (DataAccessException ex) {
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    null);
            message.setErrorMessage("Error getting list");
            try {
                connections.errorate(connect.getAuthToken(), message, connect.getGameID(), session);
            } catch (IOException exe) {
                System.out.println("Bleh");
            }
        }
        try {
            connections.broadcast(connect.getAuthToken(), msg, connect.getGameID(), session);
            assert game != null;
            connections.errorate(connect.getAuthToken(),
                    new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null,
                            game.game()), connect.getGameID(), session);
        } catch (IOException ex) {
            System.out.println("There was an error when broadcasting");
        }
    }

    private boolean checkValidToken(Session session, String authToken, UserGameCommand cmd) {
        try {
            if (userService.dao.getAuthDataByToken(authToken) == null) {
                throw new DataAccessException("");
            }
        } catch (DataAccessException ex) {
            var msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            msg.setErrorMessage("Error: no user found");
            try {
                session.getRemote().sendString(gson.toJson(msg));
                return true;
            } catch (IOException exx) {
                System.out.println("Error could not send");
            }
        }
        return false;
    }

    private void makeMove(Session session, MakeMove makeMove) {
        if(connections.isFinal(makeMove.getGameID())) {
            try {
                connections.errorate(makeMove.getAuthToken(), gameEndError(),
                        makeMove.getGameID(), session);
                return;
            } catch (IOException ex) {
                System.out.println("Make move error unsent");
            }
        }
        if (checkValidToken(session, makeMove.getAuthToken(), makeMove)) return;
        ServerMessage msg = gameService.makeMove(makeMove);
        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            try {
                connections.errorate(makeMove.getAuthToken(), msg, makeMove.getGameID(), session);
            } catch (IOException ex) {
                System.out.println("There was an error errorating");
            }
        } else {
            ServerMessage notif = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, msg.getMessage());
            msg.setMessage(null);
            ServerMessage specialMsg = checkIfEvent(msg);
            try {
                connections.announce(makeMove.getAuthToken(), msg, makeMove.getGameID(), session);
                connections.broadcast(makeMove.getAuthToken(), notif, makeMove.getGameID(), session);
                if (specialMsg != null) {
                    connections.announce(makeMove.getAuthToken(), specialMsg, makeMove.getGameID(), session);
                }
            } catch (IOException ex) {
                System.out.println("There was an error notifying the other players");
            }
        }
    }
    private ServerMessage checkIfEvent(ServerMessage msg) {
        if(msg.isWhiteCheck()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in check");
        } else if (msg.isWhiteCheckmate()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in checkmate");
        } else if (msg.isWhiteStalemate()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in stalemate");
        } else if (msg.isBlackCheck()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in check");
        } else if (msg.isBlackCheckmate()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in checkmate");
        } else if (msg.isBlackStalemate()) {
            return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in stalemate");
        } else {
            return null;
        }
    }
    private void leave(Session session, Leave leave) {

        ServerMessage msg = gameService.updateGame(leave);
        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            try {
                connections.errorate(leave.getAuthToken(), msg, leave.getGameID(), session);
            } catch (IOException ex) {
                System.out.println("Error while errorating");
            }
        } else {
            try {
                connections.broadcast(leave.getAuthToken(), msg, leave.getGameID(), session);
            } catch (IOException ex) {
                System.out.println("There was an error when announcing");
            }
            connections.remove(leave);
        }
    }

    private void resign(Session session, Resign resign) {
        if(connections.isFinal(resign.getGameID())) {
            try {
                connections.errorate(resign.getAuthToken(), gameEndError(), resign.getGameID(), session);
                return;
            } catch (IOException ex) {
                System.out.println("Resign error unsent");
            }
        }
        ServerMessage message = isValidPlayer(resign);
        if (message != null) {
            try {
                connections.errorate(resign.getAuthToken(), message, resign.getGameID(), session);
                return;
            } catch (IOException ex) {
                System.out.println("Resign error not sent");
            }
        }
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
            try {
                connections.errorate(resign.getAuthToken(), new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                        "There was an error while resigning"), resign.getGameID(), session);
            } catch (IOException exe) {
                System.out.println("A problem with errorating");
            }
        }
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                resign.getUsername() + " resigned!\n" + winner + " has won the game!!!", true);
        try {
            connections.announce(resign.getAuthToken(), msg, resign.getGameID(), session);
            connections.setFinal(resign.getGameID());
        } catch (IOException ex) {
            System.out.println("There was an error when announcing");
        }
    }
    private ServerMessage gameEndError() {
        ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
        error.setErrorMessage("Error: Game has ended");
        return error;
    }
    private ServerMessage isValidPlayer(Resign cmd) {
        var game = getGame(cmd);
        String user = null;
        AuthData authData = null;
        try {
            authData = AuthDAO.getAuthByToken(cmd.getAuthToken(), gameService.dao);
            user = authData.username();
        } catch (DataAccessException ex) {
            System.out.println("Bleh");
        }
        if (user == null) {
            return null;
        }
        assert game != null;
        ServerMessage msg;
        if (!Objects.equals(user, game.whiteUsername()) && !Objects.equals(user, game.blackUsername())) {
            msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            msg.setErrorMessage("Not authorized to do this");
            return msg;
        }
        return null;
    }

    private GameData getGame(UserGameCommand cmd) {
        ListGamesRequest req = new ListGamesRequest(cmd.getAuthToken());
        GameData game = null;
        try {
            var games = gameService.listGames(req);
            for (var g : games) {
                if (g.gameID() == cmd.getGameID()) {
                    game = g;
                    break;
                }
            }
        } catch (DataAccessException ex) {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            error.setErrorMessage("Error: could not find game");
            return null;
        }
        return game;
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
