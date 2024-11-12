package ui;

import chess.ChessGame;
import client.ServerFacade;

public class Client {
    //All the menu logic and creates and sends chessboard to ChessBoard

    public void sendChessBoard() {
        chess.ChessGame game = new chess.ChessGame();
        ChessBoard board = new ChessBoard(game.getBoard().getBoard());
        board.drawChessBoard(ChessGame.TeamColor.WHITE);
    }
}
