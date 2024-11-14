package main;

import server.Server;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = 3030;
        server.run(port);
        Client client = new Client("localhost", Integer.toString(port));
        client.initialMenu();
    }
}