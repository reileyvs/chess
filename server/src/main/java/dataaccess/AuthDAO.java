package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public default void clear() {
        //clears all data from database (maybe put in GameDAO)
    }
    public default void createAuth(AuthData authData) throws DataAccessException {
        //create authToken and store it in the database as AuthData object
    }
    public default AuthData getAuthByToken(String authToken) throws DataAccessException {
        //Retrieves AuthData by authToken
        return new AuthData("","");
    }
    public default AuthData getAuthByUsername(String username) throws DataAccessException {
        //Retrieves AuthData by username
        return new AuthData("","");
    }
    public default void deleteAuth(String authToken) throws DataAccessException {
        //deletes selected AuthData
    }
}
