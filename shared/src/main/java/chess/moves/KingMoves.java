package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class KingMoves extends PieceMovesCalculator {

  public KingMoves(ChessBoard board, ChessPosition myPosition) {
    super(board, myPosition);
  }

  public Collection<ChessMove> CalculatePieceMoves() {
    Collection<ChessMove> moves = new ArrayList<ChessMove>();



    return moves;
  }

}
