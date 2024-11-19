package websocket;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import javax.websocket.*;
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

            //noinspection Convert2Lambda
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //No implementation needed
    }

    //Todo: Put websocket endpoint calls here
}
