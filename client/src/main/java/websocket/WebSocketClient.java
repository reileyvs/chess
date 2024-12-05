package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketClient extends Endpoint {

    Session session;
    ServerMessageObserver notificationHandler;
    boolean isGameEnd;

    public WebSocketClient(String url, ServerMessageObserver notificationHandler) throws Exception {
        try {
            url = url.replace("http","ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            System.out.println("Connected to server");

            //noinspection Convert2Lambda
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if(notification.isGameEnd()) {
                        isGameEnd = true;
                    }
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Socket opened");
    }

    public void connect(String authToken, String username, int gameID) {
        try {
            var command=new Connect(UserGameCommand.CommandType.CONNECT, authToken, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error: " + ex.getMessage());
        }
    }
    public void makeMove(String authToken, String username, int gameID, ChessMove move) {
        try {
            var command = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, authToken, username,
                    gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error moving: " + ex.getMessage());
        }
    }
    public void leave(String authToken, String username, int gameID) {
        try {
            var command = new Leave(UserGameCommand.CommandType.LEAVE, authToken, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error leaving: " + ex.getMessage());
        }
    }
    public void resign(String authToken, String username, int gameID) {
        try {
            var command = new Resign(UserGameCommand.CommandType.RESIGN, authToken, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error resigning: " + ex.getMessage());
        }
    }

}
