package pl.javanexus.day9;

import java.util.LinkedList;
import java.util.List;

public class Player {

    private int playerId;
    private int score;
    private List<Integer> scoreLog;

    public Player(int playerId) {
        this.playerId = playerId;
        this.scoreLog = new LinkedList<>();
    }

    public void addPoints(int points) {
        this.score += points;
        this.scoreLog.add(points);
    }

    public int getScore() {
        return score;
    }

    public List<Integer> getScoreLog() {
        return scoreLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerId == player.playerId;
    }

    @Override
    public int hashCode() {
        return playerId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", score=" + score +
                ", numberOfPoints=" + scoreLog.size() +
                '}';
    }
}
