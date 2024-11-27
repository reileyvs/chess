package websocket.commands;

public class Connect extends UserGameCommand {
    public Connect(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, username, gameID);
    }
}
