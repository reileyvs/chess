package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove extends PieceMovesCalculator {

    public PawnMove(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();

        if(myColor == ChessGame.TeamColor.BLACK) {
            if (row > 1) {
                ChessPosition backward = new ChessPosition(row-1, column);
                if(board.getPiece(backward) == null) {
                    addMooves(row, moves, backward, column);
                }
                ChessPosition downRight = new ChessPosition(row-1, column+1);
                if (column < 8 && board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() != myColor) {
                    addMoves(moves, row, downRight);
                }
                ChessPosition downLeft=new ChessPosition(row - 1, column - 1);
                if (column > 1 && board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() != myColor) {
                    addMoves(moves, row, downLeft);

                }
            }
        }
        else {
            if (row < 8) {
                ChessPosition forward=new ChessPosition(row + 1, column);
                if (board.getPiece(forward) == null) {
                    getMoves(row, moves, forward, column);
                }
                ChessPosition upRight=new ChessPosition(row + 1, column + 1);
                if (column < 8 && (board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() != myColor)) {
                    addmoves2(moves, row, upRight);

                }
                ChessPosition upLeft=new ChessPosition(row + 1, column - 1);
                if (column > 1 && (board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor() != myColor)) {
                    addmoves2(moves, row, upLeft);

                }
            }
        }

        return moves;
    }

    private void getMoves(int row, Collection<ChessMove> moves, ChessPosition forward, int column) {
        if(row == 7) {
            moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.KNIGHT));
        } else {
            if(row == 2 && board.getPiece(new ChessPosition(row +2, column)) == null) {
                ChessPosition initial = new ChessPosition(row +2, column);
                moves.add(new ChessMove(myPosition,initial,null));
            }
            moves.add(new ChessMove(myPosition, forward, null));
        }
    }

    private void addMooves(int row, Collection<ChessMove> moves, ChessPosition backward, int column) {
        if(row == 2) {
            moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.KNIGHT));
        } else {
            if(row == 7 && board.getPiece(new ChessPosition(row -2, column)) == null) {
                ChessPosition initial = new ChessPosition(row -2, column);
                moves.add(new ChessMove(myPosition,initial,null));
            }
            moves.add(new ChessMove(myPosition, backward, null));
        }
    }

    private void addmoves2(Collection<ChessMove> moves, int row, ChessPosition upRight) {
        if(row == 7) {
            moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(myPosition, upRight, null));
        }
    }

    private void addMoves(Collection<ChessMove> moves, int row, ChessPosition position) {
        if(row == 2) {
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(myPosition, position, null));
        }
    }
}
