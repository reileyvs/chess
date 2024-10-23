package request_responses;

import model.GameData;

import java.util.Collection;
import java.util.List;

public record ListGamesResponse(List<String[]> games) {
}
