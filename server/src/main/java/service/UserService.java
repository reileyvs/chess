package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request_responses.*;
import server.Server;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    public final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public final String TAKEN = "{ \"message\": \"Error: already taken\" }";

    public RegisterResponse register(RegisterRequest user) throws DataAccessException {
        if(Objects.equals(user.username(), "") || Objects.equals(user.password(), "")
                || Objects.equals(user.email(), "")) {
            throw new DataAccessException(BAD_REQUEST);
        }
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser != null) {
            throw new DataAccessException(TAKEN);
        }
        UserData newUser = new UserData(user.username(), user.password(), user.email());
        UserDAO.createUser(newUser);
        AuthData userAuth = new AuthData(UUID.randomUUID().toString(),user.username());
        AuthDAO.createAuth(userAuth);

        return new RegisterResponse(userAuth.authToken(),userAuth.username());
    }

    public LoginResponse login(LoginRequest user) throws DataAccessException {
        UserData foundUser = UserDAO.getUser(user.username());
        if(foundUser == null || !Objects.equals(foundUser.password(), user.password())) {
            throw new DataAccessException(UNAUTHORIZED);
        }

        AuthData userAuth = new AuthData(UUID.randomUUID().toString(), foundUser.username());
        AuthDAO.createAuth(userAuth);

        return new LoginResponse(userAuth.authToken(), userAuth.username());
    }

    public LogoutResponse logout(LogoutRequest auth) throws DataAccessException {
        AuthData userAuth = AuthDAO.getAuthByToken(auth.authToken());
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        AuthDAO.deleteAuth(userAuth.authToken());
        if(AuthDAO.getAuthByToken(auth.authToken()) != null) {
            throw new DataAccessException("User cannot be deleted");
        }
        return new LogoutResponse();
    }

    UserData getUser(String username) {
        return UserDAO.getUser(username);
    }
    AuthData getAuth(String username) {
        return AuthDAO.getAuthByUsername(username);
    }
}
