package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessBoard {
    //All the board painting logic
    private ChessPiece[][] board;
    private ChessGame.TeamColor teamColor=null;
    private ChessPosition pos=null;
    private PrintStream out;
    public ChessBoard(chess.ChessBoard chessBoard) {
        this.board = chessBoard.getBoard();
    }

    public void drawChessBoard(ChessGame.TeamColor teamColor, boolean[][] validMoves, ChessPosition pos) {
        this.pos = pos;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        this.teamColor = teamColor;
        out.print(ERASE_SCREEN);

        drawHeaderFooter();
        out.print(SET_TEXT_BOLD);
        out.print(RESET_BG_COLOR);
        out.print('\n');
        if(teamColor == WHITE) {
            for (int i=7; i >= 0; i--) {
                drawRow(i, validMoves);
                out.print(RESET_BG_COLOR);
                out.print('\n');
            }
        } else {
            for (int i=0; i < 8; i++) {
                drawRow(i, validMoves);
                out.print(RESET_BG_COLOR);
                out.print('\n');
            }
        }
        drawHeaderFooter();
        out.print(RESET_BG_COLOR);
        out.print('\n');
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
    private void drawRow(int count, boolean[][] validMoves) {
        numColumns(count);
        drawSquares(count, validMoves);
        numColumns(count);
    }
    private void drawSquares(int row, boolean[][] validMoves) {
        for(int i = 0; i < 8; i++) {
            ChessPosition position = new ChessPosition(row+1,i+1);
            if (validMoves != null) {
                checking(row, validMoves, i, position);
                placeChessPiece(row, i);
            } else {
                squares(row, i);
                placeChessPiece(row, i);
            }
        }
    }

    private void squares(int row, int i) {
        if (teamColor == WHITE) {
            if ((row + i) % 2 == 0) {
                out.print(SET_BG_COLOR_BLACK);
            } else {
                out.print(SET_BG_COLOR_WHITE);
            }
        } else {
            if ((row + i) % 2 == 0) {
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
        }
    }

    private void checking(int row, boolean[][] validMoves, int i, ChessPosition position) {
        if (teamColor == WHITE) {
            checkThings(row, validMoves, i, position, SET_BG_COLOR_DARK_GREY, SET_BG_COLOR_BLACK,
                    SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE);
        } else {
            checkThings(row, validMoves, i, position, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE,
                    SET_BG_COLOR_DARK_GREY, SET_BG_COLOR_BLACK);
        }
    }

    private void checkThings(int row, boolean[][] validMoves, int i, ChessPosition position,
                             String setBgColorDarkGrey, String setBgColorBlack,
                             String setBgColorLightGrey, String setBgColorWhite) {
        if ((row + i) % 2 == 0) {
            extracted(row, validMoves, i, position, setBgColorDarkGrey, setBgColorBlack);
        } else {
            extracted(row, validMoves, i, position, setBgColorLightGrey, setBgColorWhite);
        }
    }

    private void extracted(int row, boolean[][] validMoves, int i, ChessPosition position, String setBgColorDarkGrey, String setBgColorBlack) {
        var posBlack = new ChessPosition(pos.getRow(), 9-pos.getColumn());
        if(teamColor == WHITE && position.equals(pos)) {
                out.print(SET_BG_COLOR_BLUE);
        } else if (teamColor == BLACK && position.equals(posBlack)) {
            out.print(SET_BG_COLOR_BLUE);
        } else {
            if(teamColor == BLACK) {
                if (validMoves[row][7 - i]) {
                    out.print(setBgColorDarkGrey);
                } else {
                    out.print(setBgColorBlack);
                }
            } else {
                if (validMoves[row][i]) {
                    out.print(setBgColorDarkGrey);
                } else {
                    out.print(setBgColorBlack);
                }
            }
        }
    }

    private void placeChessPiece(int count, int i) {
        ChessPiece piece;
        if(teamColor == WHITE) {
            piece=board[count][i];
        } else {
            piece=board[count][7-i];
        }
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
        int rowNum = 1 + count;
        setHeader();
        out.print(" " + rowNum + " ");
    }
    private void setHeader() {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

}
