package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO {
    public default void clear() {
        //clears all data from database (maybe put in GameDAO)
    }
    public default void createGame(GameData gameData) {
        //create new game with gameName
    }
    public default GameData getGame(String gameID) {
        //returns the game specified by its gameID
        return new GameData(1,"","","",new ChessGame());
    }
    public default Collection<GameData> listGames() {
        //returns all games from the database
        return new ArrayList<>();
    }
    public default void updateGame(GameData game) {
        //replace the chess game string corresponding to gameID
    }
}
