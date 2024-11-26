package websocket;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketClient extends Endpoint {

    Session session;
    ServerMessageObserver notificationHandler;

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
                    //ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    //notificationHandler.notify(notification);
                    System.out.println("Message received: " + message);
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

    //Todo: Put websocket endpoint calls here
    public void connect() {
        try {
            var command=new Connect(UserGameCommand.CommandType.CONNECT, "abc", 123);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error: " + ex.getMessage());
        }
    }
    public void makeMove() {
        try {
            var command = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, "abc", 123);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error moving: " + ex.getMessage());
        }
    }
    public void leave() {
        try {
            var command = new Leave(UserGameCommand.CommandType.LEAVE, "abc", 123);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error leaving: " + ex.getMessage());
        }
    }
    public void resign() {
        try {
            var command = new Resign(UserGameCommand.CommandType.RESIGN, "abc", 123);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            System.out.println("There was an error resigning: " + ex.getMessage());
        }
    }

}
