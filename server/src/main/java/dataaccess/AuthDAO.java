package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import model.AuthData;

import static java.lang.System.exit;

public class AuthDAO {
    public static final MemoryAuthDAO AUTH_DAO= new MemoryAuthDAO();
    private AuthDAO() {

    }
    public static void clear() {
        AUTH_DAO.clearAuthData();
    }
    public static void createAuth(AuthData authData) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        AUTH_DAO.addUser(authData);
        if(getAuthByToken(authData.authToken()) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    public static AuthData getAuthByToken(String authToken) {
        //Retrieves AuthData by authToken
        return AUTH_DAO.getAuthDataByToken(authToken);
    }
    public static AuthData getAuthByUsername(String username) {
        return AUTH_DAO.getAuthDataByUsername(username);
    }
    public static void deleteAuth(String authToken) {
        AUTH_DAO.deleteAuthDatum(authToken);
    }
}
