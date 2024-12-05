package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientException;
import client.ServerFacade;
import model.GameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import responses.*;
import websocket.ServerMessageObserver;
import websocket.WebSocketClient;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.WHITE;

public class Client implements ServerMessageObserver {
    //All the menu logic and creates and sends chessboard to ChessBoard
    private ServerFacade serverFacade;
    private WebSocketClient webSocketClient;
    private PrintStream out;
    private String userAuthtoken="";
    private String username="";
    private String orientationColor=null;
    private String playerColor=null;
    private ChessGame game;
    private GameData gameData;
    public Client(String url) throws Exception {
        serverFacade = new ServerFacade(url);
        webSocketClient = new WebSocketClient(url, this);
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
    public void sendChessBoard(String teamColor, ChessGame game, boolean[][] validMoves, ChessPosition pos) {
        ChessGame.TeamColor team;
        if(teamColor.equals("WHITE")) {
            team = WHITE;
        } else {
            team = ChessGame.TeamColor.BLACK;
        }
        ChessBoard board = new ChessBoard(game.getBoard());
        board.drawChessBoard(team, validMoves, pos);
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
                username = res.username();
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
                username = res.username();
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
                    playGame(userAuthtoken, username);
                    break;
                //observe
                case "4":
                    observeGame(userAuthtoken, username);
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
        if(gameName == null || gameName.isEmpty()) {
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
    private void playGame(String userAuthtoken, String username) {
        Scanner scanner = new Scanner(System.in);
        out.println("Type the number of the game you would like to play:");
        int gameIndex;
        String gameNum = scanner.nextLine();
        if(!Objects.equals(gameNum, "")) {
            gameIndex=Integer.parseInt(gameNum);
        } else {
            out.println("Error");
            return;
        }
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
                        this.game = game.game();
                        this.gameData = game;
                        playerColor = teamColor;
                        this.orientationColor= teamColor;
                        webSocketClient.connect(userAuthtoken, username, game.gameID());
                        gameMenu();
                    }
                }
            }
        } catch(ClientException ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("There was an error joining the game");
        }
    }
    private void observeGame(String userAuthtoken, String username) {
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
                    this.game = game.game();
                    this.orientationColor= "WHITE";
                    webSocketClient.connect(userAuthtoken, username, game.gameID());
                    out.println("Game joined. You are watching from white player's perspective");
                    gameMenu();
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

    private void gameMenu() {
        boolean quit=false;
        while(!quit){
            quit=checkGameInput();
        }
    }
    private boolean checkGameInput() {
        Scanner scanner = new Scanner(System.in);
        printGameMenu();
        String line = scanner.nextLine();
        boolean quit=false;
        try {
            switch(line) {
                //Redraw board
                case "1":
                    sendChessBoard(orientationColor, game, null, null);
                    break;
                //Make Move
                case "2":
                    makeMove();
                    break;
                //Highlight legal moves
                case "3":
                    highlight();
                    break;
                //leave
                case "4":
                    quit = true;
                    leave();
                    break;
                //resign
                case "5":
                    resign();
                break;
                //help
                case "6":
                    printGameHelp();
                    break;
                default:
                    out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    out.println("Invalid input");
                    printGameHelp();
                    printGameMenu();
            }
            return quit;
        } catch(Exception ex) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Something went wrong");
        }
        return quit;
    }
    private void printGameMenu() {
        out.print(EscapeSequences.RESET_TEXT_COLOR);
        out.println("\nType the number corresponding to the action you want:");
        out.println("\t1. Redraw Chess Board");
        out.println("\t2. Make Move");
        out.println("\t3. Highlight Legal Moves");
        out.println("\t4. Leave game");
        out.println("\t5. Resign (Opponent wins)");
        out.println("\t6. Help\n");
    }
    private void printGameHelp() {
        out.println("1 to see the board, 2 to move a piece, 3 to see available moves of a piece, 4 to resign," +
                " 5 to leave, and 6 to see this message again");
    }
    private void makeMove() {
        Scanner scanner = new Scanner(System.in);
        out.println("What piece would you like to move? Ex. c4");
        String piecePos = scanner.nextLine();
        ChessPosition initPos = getPosition(piecePos);
        if (initPos == null) {
            return;
        }
        out.println("Where do you want to move your piece?");
        String movePos = scanner.nextLine();
        ChessPosition finalPos = getPosition(movePos);
        if (finalPos == null) {
            return;
        }
        ChessPiece piece = game.getBoard().getPiece(initPos);
        Collection<ChessMove> moves;
        ChessPiece.PieceType promoPiece = null;
        if(piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            moves = piece.pieceMoves(game.getBoard(), initPos);
            boolean promo = false;
            for (var move : moves) {
                if (move.getStartPosition() == initPos && move.getPromotionPiece() != null) {
                    promo = true;
                    break;
                }
            }
            if (promo) {
                out.println("Promo piece");
                promoPiece = promotionPieceMenu();
            }
        }
        ChessMove move = new ChessMove(initPos, finalPos, promoPiece);
        webSocketClient.makeMove(userAuthtoken, username, gameData.gameID(), move, playerColor);
    }
    private void highlight() {
        Scanner scanner = new Scanner(System.in);
        out.println("What piece would you like to highlight? Ex. c4");
        String piecePos = scanner.nextLine();
        ChessPosition pos = getPosition(piecePos);
        if(pos == null) {
            return;
        }
        ChessPiece piece = game.getBoard().getPiece(pos);
        var moves = piece.pieceMoves(game.getBoard(), pos);
        boolean[][] validMoves = setValidMoves((ArrayList<ChessMove>) moves);
        sendChessBoard(orientationColor, game, validMoves, pos);
    }
    private void resign() {
        Scanner scanner = new Scanner(System.in);
        out.println("Are you sure you want to resign?\n Type 1 for yes and anything else for no");
        String line = scanner.nextLine();
        if(line.equals("1")) {
            webSocketClient.resign(userAuthtoken, username, gameData.gameID(), playerColor);
        }
    }
    private void leave() {
        Scanner scanner = new Scanner(System.in);
        out.println("Are you sure you want to leave?\n Type 1 for yes and anything else for no");
        String line = scanner.nextLine();
        if(line.equals("1")) {
            webSocketClient.leave(userAuthtoken, username, gameData.gameID());
        }
        playerColor=null;
    }
    private ChessPosition getPosition(String piecePos) {
        if (piecePos.length() != 2) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Incorrect position");
            return null;
        } else if (!Character.isLetter(piecePos.charAt(0)) || !Character.isDigit(piecePos.charAt(1))) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Wrong order, or incorrect values");
            return null;
        }
        return stringToPosition(piecePos);
    }
    private ChessPosition stringToPosition(final String piecePos) {
        char letter = piecePos.charAt(0);
        char number = piecePos.charAt(1);
        int col = -1;
        int row = -1;
        switch (letter) {
            case 'a', 'A' -> col = 1;
            case 'b', 'B' -> col = 2;
            case 'c', 'C' -> col = 3;
            case 'd', 'D' -> col = 4;
            case 'e', 'E' -> col = 5;
            case 'f', 'F' -> col = 6;
            case 'g', 'G' -> col = 7;
            case 'h', 'H' -> col = 8;
            default -> {
                out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                out.println("Row is out of range");
                return null;
            }
        }
        row = number - '0';
        if (row > 8 || row < 1) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println("Column is out of range");
            return null;
        }
        return new ChessPosition(row, col);
    }
    private boolean[][] setValidMoves(final ArrayList<ChessMove> moves) {
        boolean[][] moveBoard = new boolean[8][8];
        for (ChessMove move : moves) {
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            moveBoard[start.getRow() - 1][start.getColumn() - 1] = true;
            moveBoard[end.getRow() - 1][end.getColumn() - 1] = true;
        }
        return moveBoard;
    }

    private ChessPiece.PieceType promotionPieceMenu() {
        ChessPiece.PieceType promoPiece = null;
        boolean chosen=false;
        while (!chosen) {
            Scanner scanner=new Scanner(System.in);
            out.println("What do you want your pawn to become?\n1: Queen\n2: Bishop\n3: Rook (Castle)\n4: Knight (Horsey)");
            int type=Integer.getInteger(scanner.nextLine());
            switch (type) {
                case 1:
                    promoPiece=ChessPiece.PieceType.QUEEN;
                    chosen=true;
                    break;
                case 2:
                    promoPiece=ChessPiece.PieceType.BISHOP;
                    chosen=true;
                    break;
                case 3:
                    promoPiece=ChessPiece.PieceType.ROOK;
                    chosen=true;
                    break;
                case 4:
                    promoPiece=ChessPiece.PieceType.KNIGHT;
                    chosen=true;
                    break;
                default:
                    out.println("Invalid type. Try again");
            }
        }
        return promoPiece;
    }

    @Override
    public void notify(ServerMessage msg) {
        if(msg.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            if (msg.getGame() != null) {
                this.game = msg.getGame();
            }
            sendChessBoard(orientationColor, this.game, null, null);
            return;
        }
        if(msg.getMessage() != null) {
            out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
            out.println(msg.getMessage());
        } else {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println(msg.getErrorMessage());
        }
        out.print(EscapeSequences.RESET_TEXT_COLOR);
    }
}
