package websocket.commands;

public class Resign extends UserGameCommand {
    String playerColor;
    public Resign(CommandType commandType, String authToken, String username, Integer gameID, String playerColor) {
        super(commandType, authToken, username, gameID);
        this.playerColor = playerColor;
    }
    public String getPlayerColor() {
        return playerColor;
    }
}
