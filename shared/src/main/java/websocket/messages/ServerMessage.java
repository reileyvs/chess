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
    String msg;
    String error;
    ChessGame game;
    boolean gameEnd;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String msg) {
        this.serverMessageType = type;
        this.msg = msg;
        this.error = null;
    }
    public ServerMessage(ServerMessageType type, String msg, ChessGame game) {
        this.serverMessageType = type;
        this.msg = msg;
        this.error = null;
        this.game = game;
    }
    public ServerMessage(ServerMessageType type, String msg, boolean gameEnd) {
        this.serverMessageType = type;
        this.msg = msg;
        this.error = null;
        this.gameEnd = gameEnd;
    }
    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMsg() {
        return this.msg;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
    public ChessGame getGame() {
        return game;
    }
    public boolean isGameEnd() {
        return gameEnd;
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
