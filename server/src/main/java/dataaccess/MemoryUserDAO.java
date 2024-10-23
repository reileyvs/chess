package dataaccess;

import model.UserData;
import org.eclipse.jetty.security.UserAuthentication;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO {
    private final ArrayList<UserData> users;

    public MemoryUserDAO() {
        users = new ArrayList<>();
    }
    public void addUser(UserData user) {
        users.add(user);
    }
    public UserData getUser(String username) {
        for (UserData user : users) {
            if(Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    public void clearUsers() {
        users.clear();
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }
}
