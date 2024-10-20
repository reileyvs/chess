package request_responses;

public record JoinGameRequest(String authToken, String playerColor, int GameID) {
}
