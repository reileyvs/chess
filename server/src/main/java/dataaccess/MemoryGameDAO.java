package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO {
    private final List<GameData> games;

    public MemoryGameDAO() {
        games = new ArrayList<>();
    }
    public void addGame(GameData game) {
        games.removeIf(thisGame -> Objects.equals(thisGame.gameID(), game.gameID()));
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
    public List<GameData> getGames() {
        return games;
    }
    public void deleteGame(int gameID) {
        games.removeIf(game -> Objects.equals(game.gameID(), gameID));
    }
    public void clearGames() {
        games.clear();
    }
}
