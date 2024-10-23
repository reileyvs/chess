package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface GameDAO {
    MemoryGameDAO gameDb = new MemoryGameDAO();
    public static void clear() {
        gameDb.clearGames();
    }
    public static void createGame(GameData gameData) {
        //create new game with gameName
        gameDb.addGame(gameData);
    }
    public static GameData getGame(int gameID) {
        //returns the game specified by its gameID
        return gameDb.getGame(gameID);
    }
    public static List<GameData> listGames() {
        //returns all games from the database
        return gameDb.getGames();
    }
    public default void updateGame(GameData game) {
        //replace the chess game string corresponding to gameID
    }
}
