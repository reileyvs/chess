package dataaccess;

import exceptions.DataAccessException;
import model.UserData;

public interface UserDAO {

    static void clear(MySqlUserDAO dao) throws DataAccessException {
        dao.clearUsers();
    }
    static void createUser(UserData userData, MySqlUserDAO use) throws DataAccessException {
        use.addUser(userData);
        if(use.getUser(userData.username()) == null) {
            throw new DataAccessException("User could not be added");
        }
    }
    static UserData getUser(String username, MySqlUserDAO use) throws DataAccessException {
        return use.getUser(username);
    }
}
