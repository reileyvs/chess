package dataaccess;

import Exceptions.DataAccessException;
import model.UserData;

public interface UserDAO {
    MemoryUserDAO USER_DAO= new MemoryUserDAO();
    static void clear() {
        USER_DAO.clearUsers();
    }
    static void createUser(UserData userData) throws DataAccessException {
        USER_DAO.addUser(userData);
        if(USER_DAO.getUser(userData.username()) == null) {
            throw new DataAccessException("User could not be added");
        }
    }
    static UserData getUser(String username) {
        return USER_DAO.getUser(username);
    }
}
