package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessBoard {
    //All the board painting logic
    private ChessPiece[][] board;
    private ChessGame.TeamColor teamColor=null;
    private PrintStream out;
    public ChessBoard(ChessPiece[][] board) {
        this.board = board;
    }

    public void drawChessBoard(ChessGame.TeamColor teamColor) {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        this.teamColor = teamColor;
        out.print(ERASE_SCREEN);

        drawHeaderFooter();
        out.print(SET_TEXT_BOLD);
        out.print(SET_BG_COLOR_BLACK);
        out.print('\n');
        for(int i = 7; i >= 0; i--) {
            drawRow(i);
            out.print(SET_BG_COLOR_BLACK);
            out.print('\n');
        }
        drawHeaderFooter();
        out.print(SET_BG_COLOR_BLACK);
        out.print('\n');

        out.print(SET_BG_COLOR_BLACK);


    }

    private void drawHeaderFooter() {
        char alpha;
        int increment;
        if(teamColor == WHITE) {
            alpha='a';
            increment=1;
        } else {
            alpha='h';
            increment=-1;
        }
            setHeader();
            out.print("   ");
            for(int i = 0; i < 8; i++) {
                out.print(" ");
                out.print(alpha);
                out.print(" ");
                alpha=(char)(alpha+increment);
            }
            out.print("   ");
    }
    private void drawRow(int count) {
        numColumns(count);
        drawSquares(count);
        numColumns(count);
    }
    private void drawSquares(int count) {
        for(int i = 0; i < 8; i++) {
            if ((count+i) % 2 == 0) {
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
            placeChessPiece(count,i);
        }
    }
    private void placeChessPiece(int count, int i) {
        ChessPiece piece = board[count][i];
        if(piece == null) {
            out.print("   ");
        } else {
            if(piece.getTeamColor() == WHITE) {
                out.print(SET_TEXT_COLOR_YELLOW);
            } else {
                out.print(SET_TEXT_COLOR_DARK_GREEN);
            }
            out.print(" " + piece.getPieceLetter() + " ");
        }
    }
    private void numColumns(int count) {
        int rowNum;
        setHeader();
        if(teamColor == WHITE) {
            rowNum = 8 - count;

        } else {
            rowNum = 1 + count;
        }
        out.print(" " + rowNum + " ");
    }
    private void setHeader() {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

}
