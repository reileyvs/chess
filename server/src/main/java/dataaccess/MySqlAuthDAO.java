package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import chess.ChessGame;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Types.NULL;

public class MySqlAuthDAO {
    private Connection conn;
    public MySqlAuthDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public void addUser(AuthData authDatum) throws DataAccessException {
        deleteAuthDatum(authDatum.authToken());
        var statement = "INSERT INTO auth (authToken, username)" +
                "VALUES(" + authDatum.authToken() +", " + authDatum.username() + ");";
        executeUpdate(statement);


    }
    public void getAuthDataByUsername(String username) {

    }
    public void getAuthDataByToken(String authToken) {

    }
    public void deleteAuthDatum(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }
    public void clearAuthData() throws DataAccessException {
        var statement = "DROP TABLE IF EXISTS auth";
        executeUpdate(statement);
    }

    public void getAllAuthData() throws DataAccessException {
        var statement = "SELECT * FROM auth";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i=0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i+1,p);
                    else if (param instanceof Integer p) ps.setInt(i+1,p);
                    else if (param instanceof ChessGame p) ps.setString(i+1,p.toString());
                    else if (param == null) ps.setNull(i+1,NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException ex) {
            throw new RecordException("executeUpdate error");
        }
    }


    /*private void configureDatabase() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch(DataAccessException ex) {
            throw new RecordException("Database Error");
        }
        try(var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException ex) {
            throw new RecordException("Database Error");
        }
    }*/
}
