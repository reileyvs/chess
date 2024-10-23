package requestresponses;

import model.SimpleGameData;

import java.util.List;

public record ListGamesResponse(List<SimpleGameData> games) {
}
