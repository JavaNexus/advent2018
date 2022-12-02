package pl.javanexus.year2022.day2;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RockPaperScissors {

    //A for Rock, B for Paper, and C for Scissors
    private final Map<String, Symbol> opponentMapping = Map.of(
            "A", Symbol.ROCK,
            "B", Symbol.PAPER,
            "C", Symbol.SCISSORS);

    //X for Rock, Y for Paper, and Z for Scissors
    private final Map<String, Symbol> playerMapping = Map.of(
            "X", Symbol.ROCK,
            "Y", Symbol.PAPER,
            "Z", Symbol.SCISSORS);

    private final Pattern linePattern = Pattern.compile("([ABC]) ([XYZ])");

    public int calculateScore(Stream<String> lines) {
        return lines.map(line -> parseGame(line, playerMapping, opponentMapping))
                .mapToInt(Game::calculateScore)
                .sum();
    }

    private Game parseGame(String line, Map<String, Symbol> playerMapping, Map<String, Symbol> opponentMapping) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.find()) {
            Symbol opponentSymbol = opponentMapping.get(matcher.group(1));
            if (opponentSymbol == null) {
                throw new IllegalArgumentException("Unknown opponent symbol: " + matcher.group(1));
            }
            Symbol playerSymbol = playerMapping.get(matcher.group(2));
            if (playerSymbol == null) {
                throw new IllegalArgumentException("Unknown player symbol: " + matcher.group(2));
            }
            return new Game(playerSymbol, opponentSymbol);
        } else {
            throw new IllegalArgumentException("Unexpected line format: " + line);
        }
    }



    //1 for Rock, 2 for Paper, and 3 for Scissors
    enum Symbol {
        ROCK(1) {
            public boolean winsAgainst(Symbol opponentSymbol) {
                return opponentSymbol == SCISSORS;
            }
        },
        PAPER(2) {
            public boolean winsAgainst(Symbol opponentSymbol) {
                return opponentSymbol == ROCK;
            }
        },
        SCISSORS(3) {
            public boolean winsAgainst(Symbol opponentSymbol) {
                return opponentSymbol == PAPER;
            }
        },
        ;
        private final int score;

        Symbol(int score) {
            this.score = score;
        }

        public abstract boolean winsAgainst(Symbol opponentSymbol);

        //(0 if you lost, 3 if the round was a draw, and 6 if you won)
        public int getScore(Symbol opponentSymbol) {
            if (this == opponentSymbol) {
                return 3;
            } else if (winsAgainst(opponentSymbol)) {
                return 6;
            } else {
                return 0;
            }
        }
    }

    private static class Game {

        private final Symbol playerSymbol;
        private final Symbol opponentSymbol;

        public Game(Symbol playerSymbol, Symbol opponentSymbol) {
            this.playerSymbol = playerSymbol;
            this.opponentSymbol = opponentSymbol;
        }

        public int calculateScore() {
            return playerSymbol.score + playerSymbol.getScore(opponentSymbol);
        }
    }
}
