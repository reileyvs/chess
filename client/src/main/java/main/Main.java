package main;

import exceptions.DataAccessException;
import server.Server;
import ui.Client;
import websocket.WebSocketClient;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        Server server = new Server();
        int port = server.run(0);
        String host = "http://localhost:";
        Client client = new Client(host + port);
        WebSocketClient webSocketClient=null;
        try {
            webSocketClient=new WebSocketClient(host + port, client);
        } catch(Exception ex) {
            System.out.println("There was a big error");
        }
        assert webSocketClient != null;
        webSocketClient.resign();
        client.initialMenu();
    }
}
