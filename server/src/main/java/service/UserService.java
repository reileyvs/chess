package service;

import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {
        return new AuthData("","");
    }
    public AuthData login(UserData user) {
        return new AuthData("","");
    }
    public void logout(AuthData auth) {

    }
}
