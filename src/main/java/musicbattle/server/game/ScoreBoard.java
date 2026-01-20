package musicbattle.server.game;

import musicbattle.server.storage.JsonStorageService;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {

    public static final int MAX_SCORE = 5;

    private final Map<String, Integer> scores;
    private final JsonStorageService storage = new JsonStorageService();

    public ScoreBoard() {
        scores = storage.load();
    }

    public synchronized void addPlayer(String name) {
        scores.putIfAbsent(name, 0);
        storage.save(scores);
    }

    public synchronized void addPoint(String name) {
        scores.put(name, scores.getOrDefault(name, 0) + 1);
        storage.save(scores);
    }

    public synchronized boolean hasWinner() {
        return scores.values().stream().anyMatch(score -> score >= MAX_SCORE);
    }

    public synchronized String getWinner() {
        return scores.entrySet()
                .stream()
                .filter(e -> e.getValue() >= MAX_SCORE)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public synchronized String toProtocolString() {
        StringBuilder sb = new StringBuilder();
        scores.forEach((name, score) ->
                sb.append(name).append("=").append(score).append(";")
        );
        return sb.toString();
    }

    public synchronized void reset() {
        scores.clear();
        storage.save(scores);
    }
}
