package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    ChessGame game;
    boolean gameEnd;
    boolean whiteCheck;
    boolean whiteCheckmate;
    boolean whiteStalemate;
    boolean blackCheck;
    boolean blackCheckmate;
    boolean blackStalemate;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String msg) {
        this.serverMessageType = type;
        this.message= msg;
        this.errorMessage= null;
    }
    public ServerMessage(ServerMessageType type, String msg, ChessGame game) {
        this.serverMessageType = type;
        this.message= msg;
        this.errorMessage= null;
        this.game = game;
    }
    public ServerMessage(ServerMessageType type, String msg, boolean gameEnd) {
        this.serverMessageType = type;
        this.message = msg;
        this.errorMessage= null;
        this.gameEnd = gameEnd;
    }
    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return this.message;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage=errorMessage;
    }
    public void setMessage(String msg) {
        this.message = msg;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public ChessGame getGame() {
        return game;
    }
    public boolean isGameEnd() {
        return gameEnd;
    }
    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public boolean isWhiteCheck() {
        return whiteCheck;
    }
    public void setWhiteCheck(boolean whiteCheck) {
        this.whiteCheck=whiteCheck;
    }

    public boolean isWhiteCheckmate() {
        return whiteCheckmate;
    }
    public void setWhiteCheckmate(boolean whiteCheckmate) {
        this.whiteCheckmate=whiteCheckmate;
    }

    public boolean isWhiteStalemate() {
        return whiteStalemate;
    }
    public void setWhiteStalemate(boolean whiteStalemate) {
        this.whiteStalemate=whiteStalemate;
    }

    public boolean isBlackCheck() {
        return blackCheck;
    }
    public void setBlackCheck(boolean blackCheck) {
        this.blackCheck=blackCheck;
    }

    public boolean isBlackCheckmate() {
        return blackCheckmate;
    }
    public void setBlackCheckmate(boolean blackCheckmate) {
        this.blackCheckmate=blackCheckmate;
    }

    public boolean isBlackStalemate() {
        return blackStalemate;
    }
    public void setBlackStalemate(boolean blackStalemate) {
        this.blackStalemate=blackStalemate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
