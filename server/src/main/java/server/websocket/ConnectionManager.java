package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionManager {
    public final ConcurrentMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String notifier, ServerMessage msg) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(notifier)) {
                    c.send("Notification to string");
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
