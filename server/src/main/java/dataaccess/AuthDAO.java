package dataaccess;

import model.AuthData;

public interface AuthDAO {
    MemoryAuthDAO AUTH_DAO= new MemoryAuthDAO();
    static void clear() {
        AUTH_DAO.clearAuthData();
    }
    static void createAuth(AuthData authData) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        AUTH_DAO.addUser(authData);
        if(getAuthByToken(authData.authToken()) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    static AuthData getAuthByToken(String authToken) {
        //Retrieves AuthData by authToken
        return AUTH_DAO.getAuthDataByToken(authToken);
    }
    static AuthData getAuthByUsername(String username) {
        return AUTH_DAO.getAuthDataByUsername(username);
    }
    static void deleteAuth(String authToken) throws DataAccessException {
        AUTH_DAO.deleteAuthDatum(authToken);
    }
}
