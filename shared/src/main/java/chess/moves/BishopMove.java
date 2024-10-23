package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMove extends PieceMovesCalculator {

    public BishopMove(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> calculatePieceMoves() {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();

        //Diagonal up-right
        CalculateDiagonalMoves calc = new CalculateDiagonalMoves(row, column, board, myColor, myPosition, moves);
        calc.calculate();
        return moves;
    }

}