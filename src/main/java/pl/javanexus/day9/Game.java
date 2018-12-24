package pl.javanexus.day9;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {

    public static final int SCORE_POINT = 23;
    private final List<Player> players;
    private final int numberOfMarbles;

    public Game(int numberOfPlayers, int numberOfMarbles) {
        this.players = IntStream.range(0, numberOfPlayers)
                .mapToObj(Player::new)
                .collect(Collectors.toList());
        this.numberOfMarbles = numberOfMarbles;
    }

    public int getWinningScore() {
        List<Integer> marbles = new LinkedList<>();
        marbles.add(0);

        int nextMarbleIndex = 0;
        for (int marbleId = 1, countToScorePoint = 1, playerId = 0;
             marbleId <= numberOfMarbles;
             marbleId++, countToScorePoint = (countToScorePoint + 1) % SCORE_POINT, playerId = (playerId + 1) % players.size()) {
            if (countToScorePoint == 0) {
                nextMarbleIndex -= 7;
                if (nextMarbleIndex < 0) {
                    nextMarbleIndex += marbles.size();
                }
                Integer removedMarbleId = marbles.remove(nextMarbleIndex);
                System.out.printf("Player: %02d, Marbles: %d + %d\n", playerId, marbleId, removedMarbleId);
                players.get(playerId).addPoints(marbleId + removedMarbleId);
            } else {
                nextMarbleIndex += 2;
                while (nextMarbleIndex > marbles.size()) {
                    nextMarbleIndex -= marbles.size();
                }
                marbles.add(nextMarbleIndex, marbleId);
            }

        }

        Player winner = players.stream().max(Comparator.comparingInt(Player::getScore)).get();
        System.out.println("Winner: " + winner);

        players.stream()
                .sorted(Comparator.comparingInt(p -> p.getScoreLog().size()))
                .forEach(System.out::println);

        return winner.getScore();
    }
}
