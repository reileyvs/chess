package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
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
    Collection<ChessMove> moves = new ArrayList<>();
    ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
    int row = myPosition.getRow()+1;
    int column = myPosition.getColumn()+1;

    ChessPosition upRight = new ChessPosition(row+1, column+1);
    if((row < 8 && column < 8)
            && (board.getPiece(upRight) == null || board.getPiece(upRight).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,upRight,null));
    }
    ChessPosition straightUp = new ChessPosition(row+1, column);
    if((row < 8)
      && (board.getPiece(straightUp) == null || board.getPiece(straightUp).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,straightUp,null));
    }
    ChessPosition upLeft = new ChessPosition(row+1, column-1);
    if((row < 8 && column > 1)
      && (board.getPiece(upLeft) == null || board.getPiece(upLeft).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,upLeft,null));
    }
    ChessPosition right = new ChessPosition(row, column+1);
    if((column < 8)
      && (board.getPiece(right) == null || board.getPiece(right).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,right,null));
    }
    ChessPosition left = new ChessPosition(row, column-1);
    if((column > 1)
      && (board.getPiece(left) == null || board.getPiece(left).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,left,null));
    }
    ChessPosition downRight = new ChessPosition(row-1, column+1);
    if((row > 1 && column < 8)
      && (board.getPiece(downRight) == null || board.getPiece(downRight).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,downRight,null));
    }
    ChessPosition straightDown = new ChessPosition(row-1, column);
    if((row > 1)
      && (board.getPiece(straightDown) == null || board.getPiece(straightDown).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,straightDown,null));
    }
    ChessPosition downLeft = new ChessPosition(row-1, column-1);
    if((row > 1 && column > 1)
      && (board.getPiece(downLeft) == null || board.getPiece(downLeft).getTeamColor() != myColor)) {
      moves.add(new ChessMove(myPosition,downLeft,null));
    }

    return moves;
  }

}
