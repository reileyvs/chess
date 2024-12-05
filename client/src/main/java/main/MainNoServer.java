package main;

//import server.Server;
import ui.Client;

public class MainNoServer {
    public static void main(String[] args) throws Exception {
        String port = "3030";
        String host = "http://localhost:";
        Client client = new Client(host + port);
        client.initialMenu();
    }
}
