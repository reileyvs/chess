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
                    if(row == 2) {
                        moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, backward, ChessPiece.PieceType.KNIGHT));
                    } else {
                        if(row == 7 && board.getPiece(new ChessPosition(row-2,column)) == null) {
                            ChessPosition initial = new ChessPosition(row-2, column);
                            moves.add(new ChessMove(myPosition,initial,null));
                        }
                        moves.add(new ChessMove(myPosition, backward, null));
                    }
                }
                ChessPosition downRight = new ChessPosition(row-1, column+1);
                if (column < 8 && board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() != myColor) {
                    if(row == 2) {
                        moves.add(new ChessMove(myPosition, downRight, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, downRight, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, downRight, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, downRight, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, downRight, null));
                    }
                }
                ChessPosition downLeft=new ChessPosition(row - 1, column - 1);
                if (column > 1 && board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() != myColor) {
                    if(row == 2) {
                        moves.add(new ChessMove(myPosition, downLeft, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, downLeft, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, downLeft, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, downLeft, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, downLeft, null));
                    }

                }
            }
        }
        else {
            if (row < 8) {
                ChessPosition forward=new ChessPosition(row + 1, column);
                if (board.getPiece(forward) == null) {
                    if(row == 7) {
                        moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, forward, ChessPiece.PieceType.KNIGHT));
                    } else {
                        if(row == 2 && board.getPiece(new ChessPosition(row+2,column)) == null) {
                            ChessPosition initial = new ChessPosition(row+2, column);
                            moves.add(new ChessMove(myPosition,initial,null));
                        }
                        moves.add(new ChessMove(myPosition, forward, null));
                    }
                }
                ChessPosition upRight=new ChessPosition(row + 1, column + 1);
                if (column < 8 && (board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() != myColor)) {
                    if(row == 7) {
                        moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, upRight, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, upRight, null));
                    }

                }
                ChessPosition upLeft=new ChessPosition(row + 1, column - 1);
                if (column > 1 && (board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor() != myColor)) {
                    if(row == 7) {
                        moves.add(new ChessMove(myPosition, upLeft, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, upLeft, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, upLeft, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, upLeft, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, upLeft, null));
                    }

                }
            }
        }

        return moves;
    }
}
