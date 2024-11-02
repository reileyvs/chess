package service;

import Exceptions.RecordException;
import bcrypt.PasswordHasher;
import dataaccess.AuthDAO;
import Exceptions.DataAccessException;
import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlUserDAO;
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
    public MySqlAuthDAO dao;
    public MySqlUserDAO use;
    public PasswordHasher hasher = new PasswordHasher();
    public UserService(MySqlAuthDAO authDAO, MySqlUserDAO userDAO) throws DataAccessException {
        this.dao= authDAO;
        this.use = userDAO;

    }
    public RegisterResponse register(RegisterRequest user) throws DataAccessException {
        if(Objects.equals(user.username(), "") || Objects.equals(user.password(), "")
                || Objects.equals(user.email(), "") || user.username() == null
                || user.password() == null || user.email() == null) {
            throw new DataAccessException(BAD_REQUEST);
        }
        UserData foundUser = UserDAO.getUser(user.username(), use);
        if(foundUser != null) {
            throw new DataAccessException(TAKEN);
        }
        UserData newUser = new UserData(user.username(), user.password(), user.email());
        UserDAO.createUser(newUser, use);
        AuthData userAuth = new AuthData(UUID.randomUUID().toString(),user.username());
        AuthDAO.createAuth(userAuth, dao);

        return new RegisterResponse(userAuth.authToken(),userAuth.username());
    }

    public LoginResponse login(LoginRequest user) throws DataAccessException {
        UserData foundUser = UserDAO.getUser(user.username(), use);
        if(foundUser == null || !hasher.checkHash(user.password(), foundUser.password())) {
            throw new DataAccessException(UNAUTHORIZED);
        }

        AuthData userAuth = new AuthData(UUID.randomUUID().toString(), foundUser.username());
        AuthDAO.createAuth(userAuth, dao);

        return new LoginResponse(userAuth.authToken(), userAuth.username());
    }

    public LogoutResponse logout(LogoutRequest auth) throws DataAccessException {
        AuthData userAuth = AuthDAO.getAuthByToken(auth.authToken(), dao);
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        AuthDAO.deleteAuth(userAuth.authToken(), dao);
        if(AuthDAO.getAuthByToken(auth.authToken(), dao) != null) {
            throw new DataAccessException("User cannot be deleted");
        }
        return new LogoutResponse();
    }

    UserData getUser(String username) throws DataAccessException {
        return UserDAO.getUser(username, use);
    }
    AuthData getAuth(String username) throws RecordException {
        return AuthDAO.getAuthByUsername(username, dao);
    }
}
