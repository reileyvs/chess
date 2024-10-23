package server;

import dataaccess.DataAccessException;
import handlers.*;
import spark.*;


public class Server {
    public static final String GAME = "/game";
    public static final String JSON = "application/json";
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
            response.type(JSON);
            RegisterHandler handler = new RegisterHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);

            }
            return res;
        }));
        //Login
        Spark.post("/session",((request, response) -> {
            response.type(JSON);
            LoginHandler handler = new LoginHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);
            }
            return res;
        }));
        //Logout
        Spark.delete("/session",((request, response) -> {
            response.type(JSON);
            LogoutHandler handler = new LogoutHandler();

            String res;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);
            }
            return res;
        }));
        //List games
        Spark.get(GAME,((request, response) -> {
            response.type(JSON);
            ListGamesHandler handler = new ListGamesHandler();

            String res;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);
            }
            return res;
        }));
        //Create game
        Spark.post(GAME,((request, response) -> {
            response.type(JSON);
            CreateGameHandler handler = new CreateGameHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);
            }
            return res;
        }));
        //Join game
        Spark.put(GAME,((request, response) -> {
            response.type(JSON);
            JoinGameHandler handler = new JoinGameHandler();
            String res=null;
            try {
                res=handler.deserialize(request);
            } catch(DataAccessException ex) {
                return sendErrorMessage(ex, response);
            }
            return res;
        }));
        //Clear all
        Spark.delete("/db",((request, response) -> {
            response.type(JSON);
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
    private String sendErrorMessage(DataAccessException ex, Response response) {
        return switch (ex.getMessage()) {
            case BAD_REQUEST -> {
                response.status(400);
                yield ex.getMessage();
            }
            case TAKEN -> {
                response.status(403);
                yield ex.getMessage();
            }
            case UNAUTHORIZED -> {
                response.status(401);
                yield ex.getMessage();
            }
            default -> {
                response.status(500);
                yield "{ \"message\": \"Error:" + ex.getMessage() + "\" }";
            }
        };
    }
}
