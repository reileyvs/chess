package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class CalculateCardinalMoves {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public CalculateCardinalMoves(int row, int column, ChessBoard board, ChessGame.TeamColor myColor,
                                  ChessPosition myPosition, Collection<ChessMove> moves) {
        this.board=board;
        this.myPosition=myPosition;
    }
    public void calculate() {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        //Up
        for(int i = 1; i < 8; i++) {
            ChessPosition up=new ChessPosition(row + i, column);
            if (row+i <= 8 && board.getPiece(up) == null) {
                moves.add(new ChessMove(myPosition, up, null));
            } else {
                if (row+i <= 8 && board.getPiece(up).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition, up, null));
                }
                break;
            }
        }
        //Down
        for(int i = 1; i < 8; i++) {
            ChessPosition down=new ChessPosition(row - i, column);
            if (row-i >= 1 && board.getPiece(down) == null) {
                moves.add(new ChessMove(myPosition, down, null));
            } else {
                if (row-i >= 1 && board.getPiece(down).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition, down, null));
                }
                break;
            }
        }
        //Left
        for(int i = 1; i < 8; i++) {
            ChessPosition left=new ChessPosition(row, column - i);
            if (column-i >= 1 && board.getPiece(left) == null) {
                moves.add(new ChessMove(myPosition, left, null));
            } else {
                if (column-i >= 1 && board.getPiece(left).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition, left, null));
                }
                break;
            }
        }
        //Right
        for(int i = 1; i < 8; i++) {
            ChessPosition right=new ChessPosition(row, column + i);
            if (column+i <= 8 && board.getPiece(right) == null) {
                moves.add(new ChessMove(myPosition, right, null));
            } else {
                if (column+i <= 8 && board.getPiece(right).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition, right, null));
                }
                break;
            }
        }
    }
}
