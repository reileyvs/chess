package main;

import exceptions.DataAccessException;
//import server.Server;
import ui.Client;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        //Server server = new Server();
        //int port = server.run(0);
        Client client = new Client("localhost");
        client.initialMenu();
    }
}