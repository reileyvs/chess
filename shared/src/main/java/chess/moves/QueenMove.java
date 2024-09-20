package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMove extends PieceMovesCalculator {
    public QueenMove(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow()+1;
        int column = myPosition.getColumn()+1;
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

        return moves;
    }

}
