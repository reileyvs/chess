package service;

import Exceptions.RecordException;
import dataaccess.AuthDAO;
import Exceptions.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.*;
import responses.LoginResponse;
import responses.LogoutResponse;
import responses.RegisterResponse;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";
    public AuthDAO authDAO;
    public UserService() throws DataAccessException {
        authDAO = new AuthDAO();
    }
    public RegisterResponse register(RegisterRequest user) throws DataAccessException {
        if(Objects.equals(user.username(), "") || Objects.equals(user.password(), "")
                || Objects.equals(user.email(), "") || user.username() == null
                || user.password() == null || user.email() == null) {
            throw new DataAccessException(BAD_REQUEST);
        }
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser != null) {
            throw new DataAccessException(TAKEN);
        }
        UserData newUser = new UserData(user.username(), user.password(), user.email());
        UserDAO.createUser(newUser);
        AuthData userAuth = new AuthData(UUID.randomUUID().toString(),user.username());
        authDAO.createAuth(userAuth);

        return new RegisterResponse(userAuth.authToken(),userAuth.username());
    }

    public LoginResponse login(LoginRequest user) throws DataAccessException {
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser == null || !Objects.equals(foundUser.password(), user.password())) {
            throw new DataAccessException(UNAUTHORIZED);
        }

        AuthData userAuth = new AuthData(UUID.randomUUID().toString(), foundUser.username());
        authDAO.createAuth(userAuth);

        return new LoginResponse(userAuth.authToken(), userAuth.username());
    }

    public LogoutResponse logout(LogoutRequest auth) throws DataAccessException {
        AuthData userAuth = authDAO.getAuthByToken(auth.authToken());
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        authDAO.deleteAuth(userAuth.authToken());
        if(authDAO.getAuthByToken(auth.authToken()) != null) {
            throw new DataAccessException("User cannot be deleted");
        }
        return new LogoutResponse();
    }

    UserData getUser(String username) {
        return UserDAO.getUser(username);
    }
    AuthData getAuth(String username) throws RecordException {
        return authDAO.getAuthByUsername(username);
    }
}
