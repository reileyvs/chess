package dataaccess;

import model.UserData;

public interface UserDAO {
    MemoryUserDAO userDb = new MemoryUserDAO();
    MemoryAuthDAO authDb = new MemoryAuthDAO();
    public static void clear() {
        //clears all data from database (maybe put in GameDAO)
    }
    public static void createUser(UserData userData) throws DataAccessException {
        userDb.addUser(userData);
        if(userDb.getUser(userData.username()) == null) {
            throw new DataAccessException("User could not be added");
        }
    }
    public static UserData getUser(String username) {
        return userDb.getUser(username);
    }
}
