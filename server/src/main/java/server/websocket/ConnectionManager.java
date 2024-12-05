package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.Leave;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionManager {
    public final ConcurrentMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentMap<Integer, ArrayList<Object>> games = new ConcurrentHashMap<>();

    public void add(String authToken, int gameID, Session session) {
        var connection = new Connection(authToken, session);
        var yead = games.get(gameID);
        if(yead == null) {
            ArrayList<Object> cons = new ArrayList<>();
            Boolean bool = false;
            cons.add(bool);
            cons.add(connection);
            games.put(gameID, cons);
        } else {
            yead.add(connection);
            games.put(gameID, yead);
        }
    }

    public void setFinal(int gameID) {
        var game = games.get(gameID);
        if(game == null) {
            return;
        }
        Boolean isFinal = true;
        game.set(0,isFinal);
    }
    public boolean isFinal(int gameID) {
        var game = games.get(gameID);
        if(game == null) {
            return false;
        }
        return (boolean)game.getFirst();
    }

    public void remove(Leave leave) {
        connections.remove(leave.getAuthToken());
        var game = games.get(leave.getGameID());
        for (int i = 1; i < game.size(); i++) {
            Connection c = (Connection)game.get(i);
            if (Objects.equals(c.authToken, leave.getAuthToken())) {
                game.remove(i);
                break;
            }
        }
    }

    /**
     * Sends a message only to the acting client
     * @param notifier
     * @param msg
     * @throws IOException
     */
    public void errorate(String notifier, ServerMessage msg, int gameID, Session session) throws IOException {
        var removeList = new ArrayList<Connection>();
        var game = games.get(gameID);
        for (int i = 1; i < game.size(); i++) {
            Connection c = (Connection)game.get(i);
            //if (c.session.isOpen()) {
                if (c.authToken.equals(notifier)) {
                    c.send(msg);
                }
            //} else {
                //removeList.add(c);
            //}
        }

        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void broadcast(String notifier, ServerMessage msg, int gameID, Session session) throws IOException {
        var removeList = new ArrayList<Connection>();
        var game = games.get(gameID);
        for (int i = 1; i < game.size(); i++) {
            Connection c = (Connection)game.get(i);
            //if (c.session.isOpen()) {
                if (!c.authToken.equals(notifier)) {
                    c.send(msg);
                }
            //} else {
                //removeList.add(c);
            //}
        }

        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    /**
     * Sends a message to all connections, including the notifier
     * @param notifier
     * @param msg
     */
    public void announce(String notifier, ServerMessage msg, int gameID, Session session) throws IOException {
        var removeList = new ArrayList<Connection>();
        var game = games.get(gameID);
        for (int i = 1; i < game.size(); i++) {
            Connection c = (Connection)game.get(i);
            c.send(msg);
            //} else {
                //removeList.add(c);
            //}
        }

        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
}
