package ui;

import chess.ChessGame;
import client.ClientException;
import client.ServerFacade;
import exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import responses.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    //All the menu logic and creates and sends chessboard to ChessBoard
    private ServerFacade serverFacade;
    private PrintStream out;
    private String userAuthtoken="";
    public Client(String host) throws DataAccessException {
        serverFacade = new ServerFacade(host);
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }
    public void initialMenu() {
        out.print(EscapeSequences.RESET_TEXT_COLOR);
        out.println("Welcome to chess. Sign in or register to start");
        boolean successful=false;
        while(!successful){
            successful=checkInitInput();
        }
        postLoginMenu();
        System.exit(0);
    }
    public void sendChessBoard(String teamColor, ChessGame game) {
        ChessGame.TeamColor team;
        if(teamColor.equals("WHITE")) {
            team = ChessGame.TeamColor.WHITE;
        } else {
            team = ChessGame.TeamColor.BLACK;
        }
        ChessBoard board = new ChessBoard(game.getBoard().getBoard());
        board.drawChessBoard(team);
    }
    public void printInitPrompt() {
        out.print(EscapeSequences.RESET_TEXT_COLOR);
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
                out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                out.println(res.message() + " (this user may already be taken)");
            } else {
                userAuthtoken = res.authToken();
                return true;
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
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
                out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                out.println(res.message() + " (you probably put your username or password wrong)");
            } else {
                userAuthtoken = res.authToken();
                return true;
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("There was an error logging you in");
        }
        return false;
    }
    private LoginRequest printLoginHelp() {
        out.println("Type your username:");
        Scanner scanner = new Scanner(System.in);
        String existingUsername = scanner.nextLine();
        out.flush();
        out.println("Type your password:");
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
        boolean quit=false;
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
                //play game
                case "3":
                    playGame(userAuthtoken);
                    break;
                //observe
                case "4":
                    observeGame(userAuthtoken);
                    break;
                //logout
                case "5":
                    logout();
                    break;
                //quit
                case "6":
                    quit = true;
                    break;
                case "7":
                    printPostHelp();
                    break;
                default:
                    out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    out.println("Invalid input");
                    printPostHelp();
                    printPostPrompt();
            }
            return quit;
        } catch(Exception ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("That is invalid input");
        }
        return quit;
    }
    private void printPostPrompt() {
        out.print(EscapeSequences.RESET_TEXT_COLOR);
        out.println("\nType the number corresponding to the action you want:");
        out.println("1. Create game");
        out.println("2. List games");
        out.println("3. Play game");
        out.println("4. Observe game");
        out.println("5. Logout");
        out.println("6. Quit");
        out.println("7. Help\n");
    }
    private void createGame(String userAuthtoken) {
        Scanner scanner = new Scanner(System.in);
        out.println("What would you like to name this game?");
        String gameName = scanner.nextLine();
        if(gameName.equals(null) || gameName.equals("")) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Game was not created. You must put a game name!");
            return;
        }
        CreateGameRequest req = new CreateGameRequest(userAuthtoken, gameName);
        try {
            CreateGameResponse res=serverFacade.createGame(req);
            if(res.message() != null) {
                out.println(res.message());
            } else {
                out.println("Game " + gameName + " created!");
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
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
                    GameData game = res.games().get(i);
                    String white = (game.whiteUsername() != null) ? game.whiteUsername() : "---";
                    String black = (game.blackUsername() != null) ? game.blackUsername() : "---";
                    out.println(i+1 + ") " + game.gameName());
                    out.println("\tWhite player: " + white);
                    out.println("\tBlack player: " + black);
                }
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("There was an error while creating your game");
        }
    }
    private void playGame(String userAuthtoken) {
        Scanner scanner = new Scanner(System.in);
        out.println("Type the number of the game you would like to play:");
        int gameIndex = Integer.parseInt(scanner.nextLine());
        out.println("What color player do you want to be? WHITE or BLACK");
        String teamColor = scanner.nextLine();
        if(!teamColor.equals("WHITE") && !teamColor.equals("BLACK")) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Invalid color");
            return;
        }
        try {
            ListGamesResponse res = serverFacade.listGames(userAuthtoken);
            if(res.message() != null) {
                out.println(res.message());
            } else {
                if(gameIndex > res.games().size() || gameIndex < 1) {
                    out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    out.println("Invalid game number. There are currently only " + res.games().size() + " games");
                } else {
                    GameData game = res.games().get(gameIndex-1);
                    JoinGameRequest joinReq = new JoinGameRequest(userAuthtoken, teamColor, game.gameID());
                    JoinGameResponse joinRes = serverFacade.joinPlayer(joinReq);
                    if(joinRes.message() != null) {
                        out.println(joinRes.message());
                    } else {
                        out.println("Game joined");
                        sendChessBoard(teamColor, game.game());
                    }
                }
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("There was an error joining the game");
        }
    }
    private void observeGame(String userAuthtoken) {
        Scanner scanner = new Scanner(System.in);
        out.println("Type the number of the game you would like to observe:");
        int gameIndex = Integer.parseInt(scanner.nextLine());
        try {
            ListGamesResponse res = serverFacade.listGames(userAuthtoken);
            if(res.message() != null) {
                out.println(res.message());
            } else {
                if(gameIndex > res.games().size() || gameIndex < 1) {
                    out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    out.println("Invalid game number. There are currently only " + res.games().size() + " games");
                } else {
                    GameData game = res.games().get(gameIndex-1);
                    out.println("Game joined. You are watching from white player's perspective");
                    sendChessBoard("WHITE", game.game());
                }
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("There was an error joining the game");
        }
    }
    private void logout() {
        LogoutResponse res = new LogoutResponse(null);
        if(res.message() != null) {
            out.println(res.message());
        }
        initialMenu();
    }
    private void printPostHelp() {
        out.println("1 to create game, 2 to list games, 3 to play games, 4 to observe a game, 5 to logout, 6 to quit");
    }
}
