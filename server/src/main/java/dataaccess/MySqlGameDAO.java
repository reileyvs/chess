package dataaccess;

import exceptions.DataAccessException;
import chess.ChessGame;
import handlers.Serializer;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dataaccess.Update.executeUpdate;
public class MySqlGameDAO {
    private Connection conn;
    private final Serializer<ChessGame> serializer = new Serializer<>();
    public MySqlGameDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public void addGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO game(gameID,whiteUser,blackUser,gameName,game)" +
                "VALUES(?,?,?,?,?)";
        executeUpdate(
                statement,
                conn,
                game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                serializer.serialize(game.game())
        );
    }
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM game WHERE gameID='" + gameID + "'";
        GameData gameData=null;
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                gameData = getInfoFromQuery(set);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("getGame failed");
        }
        return gameData;
    }
    public List<GameData> getGames() throws DataAccessException {
        var statement = "SELECT * FROM game";
        List<GameData> gameData= new ArrayList<>();
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                gameData.add(getInfoFromQuery(set));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("getGames failed");
        }
        return gameData;
    }
    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE TABLE game";
        executeUpdate(statement,conn);
    }
    public void deleteGame(int gameID) throws DataAccessException {
        var statement = "DELETE FROM game WHERE gameID=?";
        executeUpdate(statement,conn, gameID);
    }
    private GameData getInfoFromQuery(ResultSet set) throws SQLException {
        GameData gameData;
        var id = set.getInt("gameID");
        String white=null;
        try {
            white=set.getString("whiteUser");
        } catch(SQLException ex) {
            if(!Objects.equals(ex.getMessage(), "Column 'whiteUsername' not found.")) {
                throw new SQLException(ex.getMessage());
            }
        }
        String black=null;
        try {
            black=set.getString("blackUser");
        } catch(SQLException ex) {
            if(!Objects.equals(ex.getMessage(), "Column 'blackUsername' not found.")) {
                throw new SQLException(ex.getMessage());
            }
        }
        var gameName = set.getString("gameName");
        var game = set.getString("game");
        var gameDeserialized = serializer.deserializeChessGame(game);
        var chessGame = new ChessGame(gameDeserialized);
        gameData = new GameData(id, white, black, gameName, chessGame);
        return gameData;
    }
}
