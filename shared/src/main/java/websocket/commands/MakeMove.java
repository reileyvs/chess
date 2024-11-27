package websocket.commands;

public class MakeMove extends UserGameCommand {
    public MakeMove(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, username, gameID);
    }
}
