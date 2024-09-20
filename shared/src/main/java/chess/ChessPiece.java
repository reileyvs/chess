package chess;

import chess.moves.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    @Override
    public boolean equals(Object o)  {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if (pieceType == ChessPiece.PieceType.KING) {
            KingMoves kingMoves = new KingMoves(board, myPosition);
            moves = kingMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            QueenMove queenMoves = new QueenMove(board, myPosition);
            moves = queenMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            BishopMoves bishopMoves = new BishopMoves(board, myPosition);
            moves = bishopMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            KnightMove knightMoves = new KnightMove(board, myPosition);
            //moves = knightMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            RookMove rookMoves = new RookMove(board, myPosition);
            moves = rookMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {}
        else {
            return Collections.emptyList();
        }
        return moves;
    }
}
