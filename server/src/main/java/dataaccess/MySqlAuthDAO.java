package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import chess.ChessGame;
import model.AuthData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static dataaccess.Update.executeUpdate;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AutoCloseable {
    private Connection conn;
    public MySqlAuthDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public void addUser(AuthData authDatum) throws DataAccessException {
        //deleteAuthDatumByUsername(authDatum.username());
        var statement = "INSERT INTO auth (authToken, username) VALUES(?, ?);";
        executeUpdate(statement, conn, authDatum.authToken(), authDatum.username());


    }

    public AuthData getAuthDataByUsername(String username) throws RecordException {
        var statement ="SELECT * FROM auth WHERE username='" + username + "'";
        AuthData authData=null;
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                String token = set.getString("authToken");
                authData = new AuthData(token,username);
            }
        } catch(SQLException ex) {
            ex.getStackTrace();
            throw new RecordException("getAuthDataByUsername failed");
        }
        return authData;
    }
    public AuthData getAuthDataByToken(String authToken) throws RecordException {
        var statement = "SELECT * FROM auth WHERE authToken=\'" + authToken + "\'";
        AuthData authData=null;
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                String token = set.getString("authToken");
                String username = set.getString("username");
                authData = new AuthData(token,username);
            }
        } catch(SQLException ex) {
            ex.getStackTrace();
            throw new RecordException("getAuthDataByToken failed");
        }
        return authData;
    }
    public void deleteAuthDatum(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, conn, authToken);
    }
    private void deleteAuthDatumByUsername(String username) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE username=?";
        executeUpdate(statement, conn, username);
    }
    public void clearAuthData() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        executeUpdate(statement,conn);
    }

    public Collection<AuthData> getAllAuthData() throws DataAccessException {
        Collection<AuthData> authData = new ArrayList<>();

        var statement = "SELECT * FROM auth";
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                String authToken = set.getString("authToken");
                String username = set.getString("username");
                authData.add(new AuthData(authToken,username));
            }
        } catch(SQLException ex) {
            ex.getStackTrace();
            throw new RecordException("getAllAuthData failed");
        }
        return authData;
    }

    @Override
    public void close() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
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
