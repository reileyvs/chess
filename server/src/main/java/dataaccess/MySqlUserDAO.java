package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import bcrypt.PasswordHasher;
import model.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.Update.executeUpdate;

public class MySqlUserDAO {
    private Connection conn;
    private PasswordHasher hasher = new PasswordHasher();
    public MySqlUserDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public void addUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user(username, password, email) VALUES(?,?,?)";
        executeUpdate(statement, conn, user.username(), hasher.hashPassword(user.password()), user.email());
    }
    public UserData getUser(String username) throws DataAccessException {
        var statement ="SELECT * FROM user WHERE username='" + username + "'";
        UserData userData = null;
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                String password = set.getString("password");
                String email = set.getString("email");
                userData = new UserData(username, password, email);
            }
        } catch(SQLException ex) {
            ex.getStackTrace();
            throw new RecordException("getUser failed");
        }
        return userData;
    }
    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE TABLE user";
        executeUpdate(statement, conn);
    }
    public Collection<UserData> getUsers() throws RecordException {
        Collection<UserData> userData = new ArrayList<>();

        var statement ="SELECT * FROM user";
        ResultSet set;
        try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            set = preparedStatement.executeQuery();
            while(set.next()) {
                String username = set.getString("username");
                String password = set.getString("password");
                String email = set.getString("email");
                userData.add(new UserData(username, password, email));
            }
        } catch(SQLException ex) {
            ex.getStackTrace();
            throw new RecordException("getUser failed");
        }
        return userData;
    }

}
