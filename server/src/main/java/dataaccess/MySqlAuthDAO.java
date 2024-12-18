package dataaccess;

import exceptions.DataAccessException;
import exceptions.RecordException;
import model.AuthData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.Update.executeUpdate;

public class MySqlAuthDAO {
    private Connection conn;
    public MySqlAuthDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public void addUser(AuthData authDatum) throws DataAccessException {
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
}
