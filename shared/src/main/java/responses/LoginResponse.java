package responses;

public record LoginResponse(String authToken, String username, String message) {
}
