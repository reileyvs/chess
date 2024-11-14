package ui;

import chess.ChessGame;
import client.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.ERASE_SCREEN;

public class Client {
    //All the menu logic and creates and sends chessboard to ChessBoard
    private ServerFacade serverFacade;
    private PrintStream out;
    public Client(String host, String port) {
        serverFacade = new ServerFacade(host, port);
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }
    public void initialMenu() {
        out.println("Welcome to chess. Sign in or register to start");
        checkInitInput();
    }
    public void sendChessBoard() {
        chess.ChessGame game = new chess.ChessGame();
        ChessBoard board = new ChessBoard(game.getBoard().getBoard());
        board.drawChessBoard(ChessGame.TeamColor.WHITE);
    }
    public void printInitPrompt() {
        out.println("\nType the number corresponding to the action you want:");
        out.println("1. Register");
        out.println("2. Log in");
        out.println("3. Help");
        out.println("4. Quit\n");
    }
    private void printHelpPrompt() {
        out.println("Type 1 to register, 2 to log in, 3 for help, and 4 to quit");
    }
    private void checkInitInput() {
        Scanner scanner = new Scanner(System.in);
        String line="";
        while(!line.equals("4")) {
            printInitPrompt();
            line = scanner.nextLine();

            try {
                switch(line) {
                    case "1":
                        //registerLoop();
                        break;
                    case "2":
                        //loginLoop();
                        break;
                    case "3":
                        printHelpPrompt();
                        break;
                    case "4":
                        System.exit(0);
                        break;
                    default:
                        out.println("Invalid input");
                        printHelpPrompt();
                }
            } catch(Throwable ex) {
                out.println("That is invalid input");
            }
        }
    }
}
