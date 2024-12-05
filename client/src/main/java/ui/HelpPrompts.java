package ui;

import model.UserData;
import requests.LoginRequest;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HelpPrompts {
    private PrintStream out;
    public HelpPrompts() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public static void printInitPrompt() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println("\nType the number corresponding to the action you want:");
        System.out.println("1. Register");
        System.out.println("2. Log in");
        System.out.println("3. Help");
        System.out.println("4. Quit\n");
    }
    public static void printInitHelpPrompt() {
        System.out.println("Type 1 to register, 2 to log in, 3 for help, and 4 to quit");
    }
    public static LoginRequest printLoginHelp() {
        System.out.println("Type your username:");
        Scanner scanner = new Scanner(System.in);
        String existingUsername = scanner.nextLine();
        System.out.flush();
        System.out.println("Type your password:");
        String password = scanner.nextLine();
        return new LoginRequest(existingUsername, password);
    }
    public static UserData printRegisterHelp() {
        System.out.println("Type your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Type your new password:");
        System.out.flush();
        String password = scanner.nextLine();
        System.out.println("Type your email:");
        String email = scanner.nextLine();
        return new UserData(username, password, email);
    }

    public static void printPostPrompt() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println("\nType the number corresponding to the action you want:");
        System.out.println("1. Create game");
        System.out.println("2. List games");
        System.out.println("3. Play game");
        System.out.println("4. Observe game");
        System.out.println("5. Logout");
        System.out.println("6. Quit");
        System.out.println("7. Help\n");
    }
    public static void printPostHelp() {
        System.out.println("1 to create game, 2 to list games, 3 to play games, 4 to observe a game, 5 to logout, 6 to quit");
    }
    public static void printGameMenu() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println("\nType the number corresponding to the action you want:");
        System.out.println("\t1. Redraw Chess Board");
        System.out.println("\t2. Make Move");
        System.out.println("\t3. Highlight Legal Moves");
        System.out.println("\t4. Leave game");
        System.out.println("\t5. Resign (Opponent wins)");
        System.out.println("\t6. Help\n");
    }
    public static void printGameHelp() {
        System.out.println("1 to see the board, 2 to move a piece, 3 to see available moves of a piece, 4 to resign," +
                " 5 to leave, and 6 to see this message again");
    }

}
