package ui;

import chess.ChessGame;
import client.ClientException;
import client.ServerFacade;
import model.UserData;
import requests.LoginRequest;
import responses.LoginResponse;
import responses.RegisterResponse;

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
                        register();
                        break;
                    case "2":
                        login();
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
            } catch(Exception ex) {
                out.println("That is invalid input");
            }
        }
    }

    private void register() {
        UserData user = printRegisterHelp();
        try {
            RegisterResponse res = serverFacade.register(user);
            if(res.message() != null) {
                out.println(res.message() + " (this user may already be taken)");
            }
        } catch(ClientException ex) {
            out.println("There was an error signing you in");
        }
    }
    private UserData printRegisterHelp() {
        out.println("Type your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        out.println("Type your new password:");
        out.flush();
        String password = scanner.nextLine();
        out.println("Type your email:");
        String email = scanner.nextLine();
        return new UserData(username, password, email);
    }
    private void login() {
        try {
            LoginRequest req = printLoginHelp();
            LoginResponse res = serverFacade.login(req);
            if(res.message() != null) {
                out.println(res.message() + " (you probably put your username or password wrong)");
            }
        } catch(ClientException ex) {
            out.println("There was an error logging you in");
        }
    }
    private LoginRequest printLoginHelp() {
        out.println("Type your username:");
        Scanner scanner = new Scanner(System.in);
        String existingUsername = scanner.nextLine();
        out.flush();
        out.println("Type your new password:");
        String password = scanner.nextLine();
        return new LoginRequest(existingUsername, password);
    }
}
