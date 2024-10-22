package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface GameDAO {
    MemoryGameDAO gameDb = new MemoryGameDAO();
    public default void clear() {
        //clears all data from database (maybe put in GameDAO)
    }
    public static void createGame(GameData gameData) {
        //create new game with gameName
        gameDb.addGame(gameData);
    }
    public static GameData getGame(int gameID) {
        //returns the game specified by its gameID
        return new GameData(1,"","","",new ChessGame());
    }
    public static List<GameData> listGames() {
        //returns all games from the database
        return gameDb.getGames();
    }
    public default void updateGame(GameData game) {
        //replace the chess game string corresponding to gameID
    }
}
