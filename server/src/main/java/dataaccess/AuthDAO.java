package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import model.AuthData;

import static java.lang.System.exit;

public class AuthDAO {
    public static MySqlAuthDAO AUTH_DAO;

    public AuthDAO() throws DataAccessException {
        AUTH_DAO = new MySqlAuthDAO();
    }
    public void clear() throws DataAccessException {
        AUTH_DAO.clearAuthData();
    }
    public void createAuth(AuthData authData) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        AUTH_DAO.addUser(authData);
        if(getAuthByToken(authData.authToken()) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    public AuthData getAuthByToken(String authToken) throws RecordException {
        //Retrieves AuthData by authToken
        return AUTH_DAO.getAuthDataByToken(authToken);
    }
    public AuthData getAuthByUsername(String username) throws RecordException {
        return AUTH_DAO.getAuthDataByUsername(username);
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        AUTH_DAO.deleteAuthDatum(authToken);
    }
}
