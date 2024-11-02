package dataaccess;

import exceptions.DataAccessException;
import exceptions.RecordException;
import model.AuthData;

public interface AuthDAO {

    static void clear(MySqlAuthDAO dao) throws DataAccessException {
        dao.clearAuthData();
    }
    static void createAuth(AuthData authData, MySqlAuthDAO dao) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        dao.addUser(authData);
        if(getAuthByToken(authData.authToken(), dao) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    static AuthData getAuthByToken(String authToken, MySqlAuthDAO dao) throws RecordException {
        //Retrieves AuthData by authToken
        return dao.getAuthDataByToken(authToken);
    }
    static AuthData getAuthByUsername(String username, MySqlAuthDAO dao) throws RecordException {
        return dao.getAuthDataByUsername(username);
    }
    static void deleteAuth(String authToken, MySqlAuthDAO dao) throws DataAccessException {
        dao.deleteAuthDatum(authToken);
    }
}
