package dataaccess;

import model.AuthData;

public interface AuthDAO {
    MemoryAuthDAO authDb = new MemoryAuthDAO();
    public static void clear() {
        authDb.clearAuthData();
    }
    public static void createAuth(AuthData authData) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
        authDb.addUser(authData);
        if(getAuthByToken(authData.authToken()) == null) {
            throw new DataAccessException("authData could not be saved");
        }
    }
    public static AuthData getAuthByToken(String authToken) {
        //Retrieves AuthData by authToken
        return authDb.getAuthDataByToken(authToken);
    }
    public static AuthData getAuthByUsername(String username) {
        return authDb.getAuthDataByUsername(username);
    }
    public static void deleteAuth(String authToken) throws DataAccessException {
        authDb.deleteAuthDatum(authToken);
    }
}
