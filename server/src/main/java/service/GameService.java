package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.*;
import exceptions.DataAccessException;
import exceptions.RecordException;
import model.AuthData;
import model.GameData;
import requests.*;
import responses.ClearAllResponse;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import websocket.commands.Leave;
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

    public ServerMessage updateGame(Leave leave) {
        GameData game;
        AuthData user=null;
        try {
            user = dao.getAuthDataByToken(leave.getAuthToken());
        } catch (DataAccessException ex) {
            return makeError("No valid user found");
        }
        try {
            game = gameDAO.getGame(leave.getGameID());
        } catch (DataAccessException ex) {
            return makeError("No game was found to be edited");
        }
        assert game != null;
        try {
            if (Objects.equals(game.whiteUsername(), user.username())) {
                GameDAO.createGame(new GameData(game.gameID(), null,
                        game.blackUsername(), game.gameName(), game.game()), gameDAO);
            } else if (Objects.equals(game.blackUsername(), user.username())) {
                GameDAO.createGame(new GameData(game.gameID(), game.whiteUsername(), null,
                        game.gameName(), game.game()), gameDAO);
            }
        } catch (DataAccessException ex) {
            return makeError("Trouble updating existing game");
        }
        return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                leave.getUsername() + " has left the game.");
    }

    public ClearAllResponse clearAll(ClearAllRequest request) throws DataAccessException {
        AuthDAO.clear(dao);
        GameDAO.clear(gameDAO);
        UserDAO.clear(use);
        return new ClearAllResponse(null);
    }

    public ServerMessage makeMove(MakeMove makeMove) {
        try {
            if(AuthDAO.getAuthByToken(makeMove.getAuthToken(), dao) == null) {
                return makeError("Unauthorized");
            }
        } catch (RecordException ex) {
            return makeError("Unauthorized");
        }
        GameData game;
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        boolean whiteCheck;
        boolean whiteCheckmate;
        boolean whiteStalemate;
        boolean blackCheck;
        boolean blackCheckmate;
        boolean blackStalemate;
        try {
            game = GameDAO.getGame(makeMove.getGameID(), gameDAO);
            assert game != null;
            ServerMessage msg = isValidPlayer(makeMove, game);
            if (msg != null) {
                return msg;
            }
            var board = game.game();
            board.makeMove(makeMove.getMove());
            whiteCheck = board.isInCheck(white);
            whiteCheckmate = board.isInCheckmate(white);
            whiteStalemate = board.isInStalemate(white);
            blackCheck = board.isInCheck(black);
            blackCheckmate = board.isInCheckmate(black);
            blackStalemate = board.isInStalemate(black);
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
        if (whiteCheck) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setWhiteCheck(true);
            return msg;
        } else if (whiteCheckmate) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setWhiteCheckmate(true);
            msg.setGameEnd(true);
            return msg;
        } else if (whiteStalemate) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setWhiteStalemate(true);
            msg.setGameEnd(true);
            return msg;
        } else if (blackCheck) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setBlackCheck(true);
            return msg;
        } else if (blackCheckmate) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setBlackCheckmate(true);
            msg.setGameEnd(true);
            return msg;
        } else if (blackStalemate) {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                    + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
            msg.setBlackStalemate(true);
            msg.setGameEnd(true);
            return msg;
        }
        return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, makeMove.getUsername()
                + " made a move: " + makeMoveString(makeMove.getMove()), game.game());
    }
    private ServerMessage isValidPlayer(MakeMove cmd, GameData game) {
        ServerMessage msg;
        String user = null;
        AuthData auth = null;
        try {
            auth = AuthDAO.getAuthByToken(cmd.getAuthToken(), dao);
            user = auth.username();
        } catch (DataAccessException ex) {
            System.out.println("Bleh");
        }
        if (user == null) {
            return null;
        }
        var piece = game.game().getBoard().getPiece(cmd.getMove().getStartPosition());
        if (Objects.equals(user, game.whiteUsername()) && piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return null;
            }
        } else if (Objects.equals(user, game.blackUsername()) && piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                return null;
            }
        }
        msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
        msg.setErrorMessage("Not authorized to do this");
        return msg;
    }
    private ServerMessage makeError(String error) {
        var msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
        msg.setErrorMessage("Error: " + error);
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
