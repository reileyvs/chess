package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves extends PieceMovesCalculator {

    public BishopMoves(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow()+1;
        int column = myPosition.getColumn()+1;

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