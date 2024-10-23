package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class CalculateDiagonalMoves {
    private final int row;
    private final int column;
    private final ChessBoard board;
    private final ChessGame.TeamColor myColor;
    private final ChessPosition myPosition;
    private final Collection<ChessMove> moves;

    public CalculateDiagonalMoves(int row, int column, ChessBoard board, ChessGame.TeamColor myColor,
                                  ChessPosition myPosition, Collection<ChessMove> moves) {
        this.row=row;
        this.column=column;
        this.board=board;
        this.myColor=myColor;
        this.myPosition=myPosition;
        this.moves=moves;
    }

    public void calculate() {
        //Diagonal up-right
        for (int i = 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(row+i, column+i);
            if ((row+i > 8 || column+i > 8) ||
                    (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == myColor)) {
                break;
            }
            else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition,pos,null));
                break;
            }
            else {
                moves.add(new ChessMove(myPosition,pos,null));
            }
        }

        //Diagonal down-right
        for (int i = 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(row-i, column+i);
            if ((row-i < 1 || column+i > 8) ||
                    (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == myColor)) {
                break;
            }
            else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition,pos,null));
                break;
            }
            else {
                moves.add(new ChessMove(myPosition,pos,null));
            }
        }

        //Diagonal up-left
        for (int i = 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(row-i, column-i);
            if ((row-i < 1 || column-i < 1) ||
                    (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == myColor)) {
                break;
            }
            else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition,pos,null));
                break;
            }
            else {
                moves.add(new ChessMove(myPosition,pos,null));
            }

        }

        //Diagonal down-left
        for (int i = 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(row+i, column-i);
            if ((row+i > 8 || column-i < 1) ||
                    (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == myColor)) {
                break;
            }
            else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition,pos,null));
                break;
            }
            else {
                moves.add(new ChessMove(myPosition,pos,null));
            }
        }
    }
}
