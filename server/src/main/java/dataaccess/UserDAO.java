package dataaccess;

import exceptions.DataAccessException;
import model.UserData;

public interface UserDAO {

    public static void clear(MySqlUserDAO dao) throws DataAccessException {
        dao.clearUsers();
    }
    public static void createUser(UserData userData, MySqlUserDAO use) throws DataAccessException {
        use.addUser(userData);
        if(use.getUser(userData.username()) == null) {
            throw new DataAccessException("User could not be added");
        }
    }
    public static UserData getUser(String username, MySqlUserDAO use) throws DataAccessException {
        return use.getUser(username);
    }
}
