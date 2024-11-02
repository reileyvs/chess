package dataaccess;

import Exceptions.DataAccessException;
import model.GameData;
import model.SimpleGameData;

import java.util.ArrayList;
import java.util.List;

public interface GameDAO {

    static void clear(MySqlGameDAO dao) throws DataAccessException {
        dao.clearGames();
    }
    static void createGame(GameData gameData, MySqlGameDAO dao) throws DataAccessException {
        //create new game with gameName
        if(dao.getGame(gameData.gameID()) != null) {
            dao.deleteGame(gameData.gameID());
        }
        dao.addGame(gameData);
    }
    static GameData getGame(int gameID, MySqlGameDAO dao) throws DataAccessException {
        //returns the game specified by its gameID
        return dao.getGame(gameID);
    }
    static List<SimpleGameData> listGames(MySqlGameDAO dao) throws DataAccessException {
        //returns all games from the database
        List<GameData> games = dao.getGames();
        List<SimpleGameData> gamesNoBoard=new ArrayList<>(List.of());
        for (GameData game : games) {
            gamesNoBoard.add(new SimpleGameData(game.gameID(),game.whiteUsername(),
                    game.blackUsername(),game.gameName()));
        }
        return gamesNoBoard;
    }
}
