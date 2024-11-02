package dataaccess;

import Exceptions.DataAccessException;
import Exceptions.RecordException;
import model.AuthData;

public interface AuthDAO {

    public static void clear(MySqlAuthDAO dao) throws DataAccessException {
        dao.clearAuthData();
    }
    public static void createAuth(AuthData authData, MySqlAuthDAO dao) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        dao.addUser(authData);
        if(getAuthByToken(authData.authToken(), dao) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    public static AuthData getAuthByToken(String authToken, MySqlAuthDAO dao) throws RecordException {
        //Retrieves AuthData by authToken
        return dao.getAuthDataByToken(authToken);
    }
    public static AuthData getAuthByUsername(String username, MySqlAuthDAO dao) throws RecordException {
        return dao.getAuthDataByUsername(username);
    }
    public static void deleteAuth(String authToken, MySqlAuthDAO dao) throws DataAccessException {
        dao.deleteAuthDatum(authToken);
    }
}
