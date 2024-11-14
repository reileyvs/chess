package ui;

import chess.ChessGame;
import client.ClientException;
import client.ServerFacade;
import model.GameData;
import model.SimpleGameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.ListGamesRequest;
import requests.LoginRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    //All the menu logic and creates and sends chessboard to ChessBoard
    private ServerFacade serverFacade;
    private PrintStream out;
    private String userAuthtoken="";
    public Client(String host, String port) {
        serverFacade = new ServerFacade(host, port);
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }
    public void initialMenu() {
        out.println("Welcome to chess. Sign in or register to start");
        boolean successful=false;
        while(!successful){
            successful=checkInitInput();
        }
        postLoginMenu();
        System.exit(0);
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
    private void printInitHelpPrompt() {
        out.println("Type 1 to register, 2 to log in, 3 for help, and 4 to quit");
    }
    private boolean checkInitInput() {
        Scanner scanner = new Scanner(System.in);
        String line="";

        printInitPrompt();
        line = scanner.nextLine();
        boolean successful=false;
        try {
            switch(line) {
                case "1":
                    successful = register();
                    break;
                case "2":
                    successful = login();
                    break;
                case "3":
                    printInitHelpPrompt();
                    break;
                case "4":
                    System.exit(0);
                    break;
                default:
                    out.println("Invalid input");
                    printInitHelpPrompt();
            }
            return successful;
        } catch(Exception ex) {
            out.println("That is invalid input");
        }
        return successful;
    }

    private boolean register() {
        UserData user = printRegisterHelp();
        try {
            RegisterResponse res = serverFacade.register(user);
            if(res.message() != null) {
                out.println(res.message() + " (this user may already be taken)");
            } else {
                userAuthtoken = res.authToken();
                return true;
            }
        } catch(ClientException ex) {
            out.println("There was an error signing you in");
        }
        return false;
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

    private boolean login() {
        try {
            LoginRequest req = printLoginHelp();
            LoginResponse res = serverFacade.login(req);
            if(res.message() != null) {
                out.println(res.message() + " (you probably put your username or password wrong)");
            } else {
                userAuthtoken = res.authToken();
                return true;
            }
        } catch(ClientException ex) {
            out.println("There was an error logging you in");
        }
        return false;
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

    private void postLoginMenu() {
        boolean quit=false;
        while(!quit){
            quit=checkPostInput();
        }
    }
    private boolean checkPostInput() {
        Scanner scanner = new Scanner(System.in);
        String line="";

        printPostPrompt();
        line = scanner.nextLine();
        boolean successful=false;
        try {
            switch(line) {
                //create game
                case "1":
                    createGame(userAuthtoken);
                    break;
                //list game
                case "2":
                    listGames(userAuthtoken);
                    break;
                //join game
                case "3":
                    printInitHelpPrompt();
                    break;
                //observe
                case "4":
                    System.exit(0);
                    break;
                //logout
                case "5":
                //quit
                case "6":
                default:
                    out.println("Invalid input");
                    printInitHelpPrompt();
            }
            return successful;
        } catch(Exception ex) {
            out.println("That is invalid input");
        }
        return successful;
    }
    private void printPostPrompt() {
        out.println("\nType the number corresponding to the action you want:");
        out.println("1. Create game");
        out.println("2. List games");
        out.println("3. Play game");
        out.println("4. Observe game");
        out.println("5. Logout");
        out.println("6. Quit\n");
    }
    private void createGame(String userAuthtoken) {
        Scanner scanner = new Scanner(System.in);
        out.println("What would you like to name this game?");
        String gameName = scanner.nextLine();
        CreateGameRequest req = new CreateGameRequest(userAuthtoken, gameName);
        try {
            CreateGameResponse res=serverFacade.createGame(req);
            if(res.message() != null) {
                out.println(res.message());
            } else {
                out.println("Game " + gameName + " created!");
            }
        } catch(ClientException ex) {
            out.println("There was an error while creating your game");
        }
    }
    private void listGames(String userAuthtoken) {
        try {
            ListGamesResponse res = serverFacade.listGames(userAuthtoken);
            if(res.message() != null) {
                out.println(res.message());
            } else {
                out.println("Game list:");
                for(int i = 0; i < res.games().size(); i++) {
                    SimpleGameData game = res.games().get(i);
                    out.println(i+1 + ") " + game.gameName());
                    out.println("\tWhite player: " + game.whiteUsername());
                    out.println("\tBlack player: " + game.blackUsername());
                }
            }
        } catch(ClientException ex) {
            out.println("There was an error while creating your game");
        }
    }
}
