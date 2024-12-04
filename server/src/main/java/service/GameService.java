package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.*;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import requests.*;
import responses.ClearAllResponse;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import websocket.commands.MakeMove;
import websocket.messages.ServerMessage;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameService {
    public static final String UNAUTHORIZED = "{ \"message\": \"Error: unauthorized\" }";
    public static final String BAD_REQUEST = "{ \"message\": \"Error: bad request\" }";
    public static final String TAKEN = "{ \"message\": \"Error: already taken\" }";
    final Random random = new Random();
    public final MySqlAuthDAO dao;
    public final MySqlUserDAO use;
    public final MySqlGameDAO gameDAO;
    public GameService(MySqlAuthDAO authDAO, MySqlUserDAO userDAO, MySqlGameDAO gameDAO) {
        this.dao = authDAO;
        this.use = userDAO;
        this.gameDAO = gameDAO;
    }

    public List<GameData> listGames(ListGamesRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken(), dao) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        return GameDAO.listGames(gameDAO);
    }
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        if(AuthDAO.getAuthByToken(request.authToken(), dao) == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        int gameID =Math.abs(random.nextInt());
        GameData game = new GameData(gameID,null,null,
                request.gameName(), new ChessGame());
        GameDAO.createGame(game,gameDAO);
        if(GameDAO.getGame(game.gameID(), gameDAO) == null) {
            throw new DataAccessException("Game not saved");
        }
        return new CreateGameResponse(gameID, null);
    }
    public JoinGameResponse joinGame(JoinGameRequest request) throws DataAccessException {
        //find game (if it doesn't exist ex), delete game, add updated game with new player/info
        AuthData userAuth = AuthDAO.getAuthByToken(request.authToken(), dao);
        if(userAuth == null) {
            throw new DataAccessException(UNAUTHORIZED);
        }
        GameData game = GameDAO.getGame(request.gameID(), gameDAO);
        GameData updatedGame = null;
        if(game == null) {
            throw new DataAccessException(BAD_REQUEST);
        }
        if(Objects.equals(request.playerColor(), "WHITE")) {
            if(!Objects.equals(game.whiteUsername(), null)) {
                throw new DataAccessException(TAKEN);
            }
            updatedGame = new GameData(game.gameID(), userAuth.username(), game.blackUsername(),
                    game.gameName(), game.game());
        } else if (Objects.equals(request.playerColor(), "BLACK")) {
            if(!Objects.equals(game.blackUsername(), null)) {
                throw new DataAccessException(TAKEN);
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), userAuth.username(),
                    game.gameName(), game.game());
        } else {
            throw new DataAccessException(BAD_REQUEST);
        }
        GameDAO.createGame(updatedGame, gameDAO);
        return new JoinGameResponse(null);
    }

    public ClearAllResponse clearAll(ClearAllRequest request) throws DataAccessException {
        AuthDAO.clear(dao);
        GameDAO.clear(gameDAO);
        UserDAO.clear(use);
        return new ClearAllResponse(null);
    }

    public ServerMessage makeMove(MakeMove makeMove) {
        GameData game = null;
        ChessGame.TeamColor turn;

        try {
            game = GameDAO.getGame(makeMove.getGameID(), gameDAO);
            turn = game.game().getTeamTurn();
            /*if(turn == ChessGame.TeamColor.WHITE) {
                if (!makeMove.getUsername().equals(game.whiteUsername())) {
                    return makeError("It is not your turn");
                }
            } else {
                if (!makeMove.getUsername().equals(game.blackUsername())) {
                    return makeError("It is not your turn");
                }
            }*/
            game.game().makeMove(makeMove.getMove());
        } catch (DataAccessException ex) {
            return makeError("Game could not be retrieved from database");
        } catch (InvalidMoveException ex) {
            return makeError("You tried making an invalid move: " + ex.getMessage());
        }
        try {
            GameDAO.createGame(game, gameDAO);
        } catch (DataAccessException ex) {
            return makeError("Move could not be registered to the database");
        }
        return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
    }

    private ServerMessage makeError(String error) {
        var msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
        msg.setError(error);
        return msg;
    }

    private String makeMoveString(ChessMove move) {
        var initPos = move.getStartPosition();
        var endPos = move.getEndPosition();
        char initCol;
        char endCol;
        int initRow;
        int endRow;
        initCol = intToChar(initPos.getColumn());
        endCol = intToChar(endPos.getColumn());
        initRow = initPos.getRow();
        endRow = endPos.getRow();
        return "" + initCol + initRow + "->" + endCol + endRow;
    }
    private char intToChar(int col) {
        char charCol = 'z';
        switch (col) {
            case 1 -> charCol = 'a';
            case 2 -> charCol = 'b';
            case 3 -> charCol = 'c';
            case 4 -> charCol = 'd';
            case 5 -> charCol = 'e';
            case 6 -> charCol = 'f';
            case 7 -> charCol = 'g';
            case 8 -> charCol = 'h';
        }
        return charCol;
    }


}
