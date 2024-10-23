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
    public static List<String[]> listGames() {
        //returns all games from the database
        List<GameData> games = gameDb.getGames();
        List<String[]> gamesNoBoard=new ArrayList<>(List.of());
        for (GameData game : games) {
            gamesNoBoard.add(new String[]{String.valueOf(game.gameID()),game.whiteUsername(),
                    game.blackUsername(),game.gameName()});
        }
        return gamesNoBoard;
    }
    public default void updateGame(GameData game) {
        //replace the chess game string corresponding to gameID
    }
}
