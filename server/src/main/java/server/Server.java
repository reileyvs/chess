package server;

import dataaccess.DataAccessException;
import handlers.*;
import request_responses.CreateGameRequest;
import request_responses.LogoutRequest;
import spark.*;

import java.lang.reflect.Array;
import java.util.Set;


public class Server {
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void createRoutes() {

        //Create user
        Spark.post("/user",((request, response) -> {
            response.type("application/json");
            RegisterHandler handler = new RegisterHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                switch (ex.getMessage()) {
                    case BAD_REQUEST:
                        response.status(400);
                        return ex.getMessage();
                    case TAKEN:
                        response.status(403);
                        return ex.getMessage();
                    default:
                        response.status(500);
                        return "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
                }
            }
            return res;
        }));
        //Login
        Spark.post("/session",((request, response) -> {
            response.type("application/json");
            LoginHandler handler = new LoginHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                switch (ex.getMessage()) {
                    case UNAUTHORIZED:
                        response.status(401);
                        return ex.getMessage();
                    default:
                        response.status(500);
                        return "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
                }
            }
            return res;
        }));
        //Logout
        Spark.delete("/session",((request, response) -> {
            response.type("application/json");
            LogoutHandler handler = new LogoutHandler();

            String res;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                switch (ex.getMessage()) {
                    case UNAUTHORIZED:
                        response.status(401);
                        return ex.getMessage();
                    default:
                        response.status(500);
                        return "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
                }
            }
            return res;
        }));
        //List games
        Spark.get("/game",((request, response) -> {
            response.type("application/json");
            ListGamesHandler handler = new ListGamesHandler();

            String res;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                if (ex.getMessage().equals(UNAUTHORIZED)) {
                    response.status(401);
                    return ex.getMessage();
                }
                response.status(500);
                return "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
            }
            return res;
        }));
        //Create game
        Spark.post("/game",((request, response) -> {
            response.type("application/json");
            CreateGameHandler handler = new CreateGameHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                if (ex.getMessage().equals(UNAUTHORIZED)) {
                    response.status(401);
                    return ex.getMessage();
                }
                response.status(500);
                return "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
            }
            return res;
        }));
        //Join game
        Spark.put("/game",((request, response) -> {
            response.type("application/json");
            JoinGameHandler handler = new JoinGameHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return switch (ex.getMessage()) {
                    case UNAUTHORIZED -> {
                        response.status(401);
                        yield ex.getMessage();
                    }
                    case BAD_REQUEST -> {
                        response.status(400);
                        yield ex.getMessage();
                    }
                    case TAKEN -> {
                        response.status(403);
                        yield ex.getMessage();
                    }
                    default -> {
                        response.status(500);
                        yield "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
                    }
                };
            }
            return res;
        }));
        //Clear all
        Spark.delete("/db",((request, response) -> {
            response.type("application/json");
            ClearAllHandler handler = new ClearAllHandler();
            String res=null;
            res=handler.deserialize(request);
            return res;
        }));

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
