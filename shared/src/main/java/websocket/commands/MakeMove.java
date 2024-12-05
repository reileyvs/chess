package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    ChessMove move;
    String playerColor;
    public MakeMove(CommandType commandType, String authToken, String username,
                    Integer gameID, ChessMove move, String playerColor) {
        super(commandType, authToken, username, gameID);
        this.move = move;
        this.playerColor = playerColor;
    }
    public ChessMove getMove() {
        return move;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
