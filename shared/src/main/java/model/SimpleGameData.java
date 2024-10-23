package model;

import chess.ChessGame;

public record SimpleGameData(int gameID, String whiteUsername, String blackUsername, String gameName) {
}