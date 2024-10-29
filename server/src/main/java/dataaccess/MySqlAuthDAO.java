package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;

import java.sql.SQLException;

public class MySqlAuthDAO {
    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements ={
            """
            Here is the SQL CREATE TABLE for AuthData storing
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException ex) {
            throw new RecordException("Database Error");
        }
    }
}
