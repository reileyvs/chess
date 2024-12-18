package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.lang.System.exit;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }
    public ChessGame(ChessGame game) {
        this.teamTurn = game.teamTurn;
        this.board = game.board;
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        if(board.getPiece(startPosition) == null) {
            return Collections.emptyList();
        }
        TeamColor pieceColor = board.getPiece(startPosition).getTeamColor();
        //InvalidMoveException when King is put in check or when not your turn

        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        //For each move, make move and then check to see which moves DON'T put king in check
        for(ChessMove move : moves) {
            ChessBoard boardClone=tryClone();
            ChessBoard boardCloneSafe=tryClone();
            movePiece(move, boardClone, move.getPromotionPiece());
            setBoard(boardClone);

            if (!isInCheck(pieceColor)) {
                validMoves.add(move);
            }
            setBoard(boardCloneSafe);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //If a valid move:
        //Set array location of endPos to chessPiece at startPos
        if(board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece to move");
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your turn");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if(validMoves.isEmpty() && (isInCheckmate(getTeamTurn()) || isInStalemate(getTeamTurn()))) {
            throw new InvalidMoveException("No moves available");
        }
        boolean isValid = false;
        //Is the move you're making valid as found in validMoves?
        for (ChessMove validMove : validMoves) {
            if (validMove.equals(move)) {
                isValid=true;
                break;
            }
        }
        if(isValid) {
            //If so, move the piece
            movePiece(move, getBoard(), move.getPromotionPiece());
            //And change whose turn it is
            if(teamTurn == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Not a valid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean isInCheck = false;

        //Get king position
        ChessPosition kingPosition = getKingPosition(teamColor);
        if(kingPosition == null) {
            return false;
        }
        //If any move from the enemy lands on your king, you are in check
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                isInCheck = checkDangerousMove(pos, kingPosition, teamColor);
                if(isInCheck) {
                    break;
                }
            }
            if(isInCheck) {
                break;
            }
        }
        return isInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean inCheckMate=true;

        //Get king position
        ChessPosition kingPosition = getKingPosition(teamColor);
        if(kingPosition == null) {
            return false;
        }
        //If no available move from you results in !isInCheck(), you are in checkMate
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition pos=new ChessPosition(i, j);
                ChessPiece piece=board.getPiece(pos);
                inCheckMate = loopCheck(pos, teamColor, inCheckMate, piece);
                if(!inCheckMate) {
                    break;
                }
            }
            if(!inCheckMate) {
                break;
            }
        }
        return inCheckMate;
    }
    private boolean loopCheck(ChessPosition pos, TeamColor teamColor, boolean inCheckMate, ChessPiece piece) {
        for (ChessMove move : validMoves(pos)) {
            if (board.getPiece(move.getStartPosition()) != null
                    && board.getPiece(move.getStartPosition()).getTeamColor() == teamColor) {
                ChessBoard clone=tryClone();
                ChessBoard cloneSafe = tryClone();
                movePiece(move, clone, move.getPromotionPiece());
                setBoard(clone);
                if(!isInCheck(teamColor)) {
                    inCheckMate = false;
                    setBoard(cloneSafe);
                    break;
                }
                setBoard(cloneSafe);
            }
        }
        return inCheckMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean inStalemate = true;
        boolean areValidMoves = false;

        //If no valid moves, you are in stalemate
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor) {
                    areValidMoves = !validMoves(pos).isEmpty();
                }
                if(areValidMoves) {
                    inStalemate = false;
                    break;
                }
            }
            if(areValidMoves) {
                break;
            }
        }
        if(isInCheck(teamColor)) {
            inStalemate = false;
        }
        return inStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        //Will replace board with new board
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private void movePiece(ChessMove move, ChessBoard board, ChessPiece.PieceType promotionPiece ) {
        if(promotionPiece == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        } else {
            ChessPiece pawnPromote = new ChessPiece(
                    board.getPiece(move.getStartPosition()).getTeamColor(), promotionPiece
                    );
            board.addPiece(move.getEndPosition(), pawnPromote);
        }
        board.addPiece(move.getStartPosition(), null);
    }
    private ChessBoard tryClone() {
        ChessBoard board=null;
        try {
            board=(ChessBoard) this.board.clone();
        } catch(CloneNotSupportedException ex) {
            ex.printStackTrace();
            System.out.println("Clone failed");
            exit(1);
        }
        return board;
    }
    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition kingPosition=null;
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if (board.getPiece(pos) != null
                        && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING
                        && board.getPiece(pos).getTeamColor() == teamColor) {
                    kingPosition = pos;
                }
            }
        }
        return kingPosition;
    }
    private Boolean checkDangerousMove(ChessPosition pos, ChessPosition kingPos, TeamColor teamColor) {
        if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != teamColor) {
            for (ChessMove enemyMove : board.getPiece(pos).pieceMoves(board, pos)) {
                if(enemyMove.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

}
