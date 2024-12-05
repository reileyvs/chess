package websocket.commands;

public class Leave extends UserGameCommand {
    public Leave(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, username, gameID);
    }

}
