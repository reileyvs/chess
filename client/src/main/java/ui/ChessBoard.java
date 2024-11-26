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
    public ChessBoard(chess.ChessBoard chessBoard) {
        this.board = chessBoard.getBoard();
    }

    public void drawChessBoard(ChessGame.TeamColor teamColor, boolean[][] validMoves) {
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
            if (validMoves != null) {
                if (teamColor == WHITE) {
                    if ((row + i) % 2 == 0) {
                        if(validMoves[row][i]) {
                            out.print(SET_BG_COLOR_DARK_GREY);
                        } else {
                            out.print(SET_BG_COLOR_BLACK);
                        }
                    } else {
                        if(validMoves[row][i]) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                        } else {
                            out.print(SET_BG_COLOR_WHITE);
                        }
                    }
                } else {
                    if ((row + i) % 2 == 0) {
                        if(validMoves[row][i]) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                        } else {
                            out.print(SET_BG_COLOR_WHITE);
                        }
                    } else {
                        if(validMoves[row][i]) {
                            out.print(SET_BG_COLOR_DARK_GREY);
                        } else {
                            out.print(SET_BG_COLOR_BLACK);
                        }
                    }
                }
                placeChessPiece(row, i);
            } else {
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
                placeChessPiece(row, i);
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
