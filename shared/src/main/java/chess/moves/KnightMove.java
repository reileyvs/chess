package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMove extends PieceMovesCalculator {

    public KnightMove(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();

        //Up-left and up-right
        if(row <= 6) {
            if(column > 1) {
                ChessPosition upLeft = new ChessPosition(row + 2, column - 1);
                if (board.getPiece(upLeft) == null || board.getPiece(upLeft).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition, upLeft, null));
                }
            }
            if(column <= 7) {
                ChessPosition upRight = new ChessPosition(row+2, column+1);
                if(board.getPiece(upRight) == null || board.getPiece(upRight).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,upRight,null));
                }
            }
        }
        //Down-left and down-right
        if(row > 2) {
            if(column > 1) {
                ChessPosition downLeft = new ChessPosition(row-2, column-1);
                if(board.getPiece(downLeft) == null || board.getPiece(downLeft).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,downLeft,null));
                }
            }
            if(column <= 7) {
                ChessPosition downRight = new ChessPosition(row-2, column+1);
                if(board.getPiece(downRight) == null || board.getPiece(downRight).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,downRight,null));
                }
            }
        }
        //Left-up and left-down
        if(column > 2) {
            if(row > 1) {
                ChessPosition leftDown = new ChessPosition(row-1, column-2);
                if(board.getPiece(leftDown) == null || board.getPiece(leftDown).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,leftDown,null));
                }
            }
            if(row <= 7) {
                ChessPosition leftUp = new ChessPosition(row+1,column-2);
                if(board.getPiece(leftUp) == null || board.getPiece(leftUp).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,leftUp,null));
                }
            }
        }
        //Right-up and right-down
        if(column <= 6) {
            if(row > 1) {
                ChessPosition rightUp = new ChessPosition(row-1, column+2);
                if(board.getPiece(rightUp) == null || board.getPiece(rightUp).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,rightUp,null));
                }
            }
            if(row <= 7) {
                ChessPosition rightDown = new ChessPosition(row+1, column+2);
                if(board.getPiece(rightDown) == null || board.getPiece(rightDown).getTeamColor() != myColor) {
                    moves.add(new ChessMove(myPosition,rightDown,null));
                }
            }
        }

        return moves;
    }



}
