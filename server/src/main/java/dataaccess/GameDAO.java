package dataaccess;

import model.GameData;
import model.SimpleGameData;

import java.util.ArrayList;
import java.util.List;

public interface GameDAO {
    MemoryGameDAO GAME_DAO = new MemoryGameDAO();


    static void clear() {
        GAME_DAO.clearGames();
    }
    static void createGame(GameData gameData) {
        //create new game with gameName
        GAME_DAO.addGame(gameData);
    }
    static GameData getGame(int gameID) {
        //returns the game specified by its gameID
        return GAME_DAO.getGame(gameID);
    }
    static List<SimpleGameData> listGames() {
        //returns all games from the database
        List<GameData> games = GAME_DAO.getGames();
        List<SimpleGameData> gamesNoBoard=new ArrayList<>(List.of());
        for (GameData game : games) {
            gamesNoBoard.add(new SimpleGameData(game.gameID(),game.whiteUsername(),
                    game.blackUsername(),game.gameName()));
        }
        return gamesNoBoard;
    }
}
