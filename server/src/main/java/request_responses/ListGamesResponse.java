package request_responses;

import model.GameData;
import model.SimpleGameData;

import java.util.Collection;
import java.util.List;

public record ListGamesResponse(List<SimpleGameData> games) {
}
