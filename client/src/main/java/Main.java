import chess.*;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("localhost", "3030");
        client.initialMenu();
    }
}