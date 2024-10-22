package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request_responses.LoginRequest;
import request_responses.LogoutRequest;
import request_responses.RegisterRequest;
import request_responses.RegisterResponse;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    public RegisterResponse register(RegisterRequest user) throws DataAccessException {
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser != null) {
            throw new DataAccessException("User already exists.");
        }
        UserData newUser = new UserData(user.username(), user.password(), user.email());
        UserDAO.createUser(newUser);
        AuthData userAuth = new AuthData(user.username(),UUID.randomUUID().toString());
        AuthDAO.createAuth(userAuth);

        return new RegisterResponse(userAuth.username(),userAuth.authToken());
    }

    public RegisterResponse login(LoginRequest user) throws DataAccessException {
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser == null) {
            throw new DataAccessException("No user " + user.username() + " exists");
        }
        if(!Objects.equals(foundUser.password(), user.password())) {
            throw new DataAccessException("Password is incorrect");
        }

        AuthData userAuth = new AuthData(foundUser.username(), UUID.randomUUID().toString());
        AuthDAO.createAuth(userAuth);

        return new RegisterResponse(userAuth.username(), userAuth.authToken());
    }

    public void logout(LogoutRequest auth) throws DataAccessException {
        AuthData userAuth = AuthDAO.getAuthByToken(auth.authToken());
        if(userAuth == null) {
            throw new DataAccessException("User is not validated");
        }
        AuthDAO.deleteAuth(userAuth.authToken());
        if(AuthDAO.getAuthByToken(auth.authToken()) != null) {
            throw new DataAccessException("User cannot be deleted");
        }
    }

    UserData getUser(String username) {
        return UserDAO.getUser(username);
    }
    AuthData getAuth(String username) {
        return AuthDAO.getAuthByUsername(username);
    }
}
