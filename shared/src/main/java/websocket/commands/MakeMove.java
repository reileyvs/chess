package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    ChessMove move;
    public MakeMove(CommandType commandType, String authToken, String username, Integer gameID, ChessMove move) {
        super(commandType, authToken, username, gameID);
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }
}
