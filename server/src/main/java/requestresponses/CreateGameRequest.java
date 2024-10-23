package requestresponses;

public record CreateGameRequest(String authToken, String gameName) {
}
