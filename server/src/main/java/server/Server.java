package server;

import Exceptions.DataAccessException;
import dataaccess.*;
import handlers.*;
import spark.*;

import static java.lang.System.exit;


public class Server {
    public static final String GAME = "/game";
    public static final String JSON = "application/json";
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        MySqlAuthDAO authDAO=null;
        MySqlUserDAO userDAO=null;
        MySqlGameDAO gameDAO=null;

        // create SQL database
        try {
            DatabaseManager.createDatabase();
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();
            gameDAO = new MySqlGameDAO();
        } catch(DataAccessException ex) {
            ex.getStackTrace();
            exit(1);
        }
        createRoutes(authDAO, userDAO, gameDAO);

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void createRoutes(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        //Create user
        Spark.post("/user",((request, response) -> {
            response.type(JSON);
            RegisterHandler handler = new RegisterHandler(authDAO,userDAO);
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
            LoginHandler handler = new LoginHandler(authDAO,userDAO);
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
            LogoutHandler handler = new LogoutHandler(authDAO,userDAO);

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
            ListGamesHandler handler = new ListGamesHandler(authDAO,userDAO,gameDAO);

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
            CreateGameHandler handler = new CreateGameHandler(authDAO,userDAO,gameDAO);
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
            JoinGameHandler handler = new JoinGameHandler(authDAO,userDAO,gameDAO);
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
            ClearAllHandler handler = new ClearAllHandler(authDAO,userDAO,gameDAO);
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
                yield "{ \"message\": \"Error: " + ex.getMessage().replace("\"", "\\\"") + "\" }";
            }
        };
    }
}
