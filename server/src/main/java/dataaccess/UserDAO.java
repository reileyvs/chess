package dataaccess;

import model.UserData;

public interface UserDAO {
    public default void clear() {
        //clears all data from database (maybe put in GameDAO)
    }
    public default void createUser(UserData userData) throws DataAccessException {
        //creates user in database with username and password
    }
    public default UserData getUser(String username) throws DataAccessException {
        //gets User by username from database and returns a UserData object
        return new UserData("","","");
    }
}
