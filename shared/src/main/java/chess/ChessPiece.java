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
    private char pieceLetter;
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    @Override
    public boolean equals(Object o)  {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    public ChessPiece clone() {
        return new ChessPiece(this.pieceColor, this.getPieceType());
    }


    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        switch (this.pieceType) {
            case KING -> this.pieceLetter = 'K';
            case QUEEN -> this.pieceLetter = 'Q';
            case BISHOP -> this.pieceLetter = 'B';
            case KNIGHT -> this.pieceLetter = 'H';
            case ROOK -> this.pieceLetter = 'R';
            case PAWN -> this.pieceLetter = 'P';
        }

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
        Collection<ChessMove> moves;
        if (pieceType == ChessPiece.PieceType.KING) {
            KingMoves kingMoves = new KingMoves(board, myPosition);
            moves = kingMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            QueenMove queenMoves = new QueenMove(board, myPosition);
            moves = queenMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            BishopMove bishopMove= new BishopMove(board, myPosition);
            moves = bishopMove.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            KnightMove knightMoves = new KnightMove(board, myPosition);
            moves = knightMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            RookMove rookMoves = new RookMove(board, myPosition);
            moves = rookMoves.calculatePieceMoves();
        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {
            PawnMove pawnMove = new PawnMove(board, myPosition);
            moves = pawnMove.calculatePieceMoves();
        }
        else {
            return Collections.emptyList();
        }
        return moves;
    }
    public char getPieceLetter() {
        //For ui letter representation on the chessboard
        return this.pieceLetter;
    }
}
