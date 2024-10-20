package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO {
    private ArrayList<GameData> games;

    public MemoryGameDAO() {
        games = new ArrayList<GameData>();
    }
    public void addGame(GameData game) {
        games.add(game);
    }
    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if(Objects.equals(game.gameID(), gameID)) {
                return game;
            }
        }
        return null;
    }
    public void deleteGame(int gameID) {
        games.removeIf(game -> Objects.equals(game.gameID(), gameID));
    }
    public void clearGames() {
        games.clear();
    }
}
