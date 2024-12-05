package websocket.commands;

public class Resign extends UserGameCommand {
    public Resign(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, username, gameID);

    }
}
