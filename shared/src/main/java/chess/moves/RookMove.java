package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMove extends PieceMovesCalculator {

    public RookMove(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        //Up
        CalculateCardinalMoves calc = new CalculateCardinalMoves(row, column, board, myColor, myPosition, moves);
        calc.calculate();

        return moves;
    }
}
