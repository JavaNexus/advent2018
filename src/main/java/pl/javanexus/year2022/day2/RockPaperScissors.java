package pl.javanexus.year2022.day2;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    private final Map<Symbol, Symbol> winsAgainstSymbol = Map.of(
            Symbol.ROCK, Symbol.SCISSORS,
            Symbol.PAPER, Symbol.ROCK,
            Symbol.SCISSORS, Symbol.PAPER
    );

    private final Map<Symbol, Symbol> losesAgainstSymbol = Map.of(
            Symbol.ROCK, Symbol.PAPER,
            Symbol.PAPER, Symbol.SCISSORS,
            Symbol.SCISSORS, Symbol.ROCK
    );

    private final Map<String, Result> resultMapping = Arrays.stream(Result.values())
            .collect(Collectors.toMap(result -> result.symbol, Function.identity()));

    private final Pattern linePattern = Pattern.compile("([ABC]) ([XYZ])");

    public int calculateScore(Stream<String> lines) {
        return lines.map(line -> parseGame(line, playerMapping, opponentMapping))
                .mapToInt(game -> game.calculateScore(winsAgainstSymbol))
                .sum();
    }

    public int calculateScoreWithSelectedSymbol(Stream<String> lines) {
        return lines.map(line -> selectPlayerSymbol(line, opponentMapping, resultMapping))
                .mapToInt(game -> game.calculateScore(winsAgainstSymbol))
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

    private Game selectPlayerSymbol(String line, Map<String, Symbol> opponentMapping,
                                    Map<String, Result> resultMapping) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.find()) {
            Symbol opponentSymbol = opponentMapping.get(matcher.group(1));
            if (opponentSymbol == null) {
                throw new IllegalArgumentException("Unknown opponent symbol: " + matcher.group(1));
            }
            Result expectedResult = resultMapping.get(matcher.group(2));
            if (expectedResult == null) {
                throw new IllegalArgumentException("Unknown player symbol: " + matcher.group(2));
            }
            Symbol playerSymbol =
                    selectPlayerSymbol(opponentSymbol, expectedResult, winsAgainstSymbol, losesAgainstSymbol);
            return new Game(playerSymbol, opponentSymbol);
        } else {
            throw new IllegalArgumentException("Unexpected line format: " + line);
        }
    }

    private Symbol selectPlayerSymbol(Symbol opponentSymbol, Result expectedResult,
                                      Map<Symbol, Symbol> winsAgainstSymbol, Map<Symbol, Symbol> losesAgainstSymbol) {
        switch (expectedResult) {
            case DRAW:
                return opponentSymbol;
            case PLAYER_WINS:
                return losesAgainstSymbol.get(opponentSymbol);
            case PLAYER_LOSES:
                return winsAgainstSymbol.get(opponentSymbol);
            default:
                throw new IllegalArgumentException("Unexpected result: " + expectedResult);
        }
    }

    //(0 if you lost, 3 if the round was a draw, and 6 if you won)
    //X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
    enum Result {
        PLAYER_WINS("Z", 6),
        PLAYER_LOSES("X", 0),
        DRAW("Y", 3),
        ;

        private final String symbol;
        private final int score;

        Result(String symbol, int score) {
            this.symbol = symbol;
            this.score = score;
        }
    }

    //1 for Rock, 2 for Paper, and 3 for Scissors
    enum Symbol {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
        ;
        private final int score;

        Symbol(int score) {
            this.score = score;
        }

        public boolean winsAgainst(Symbol opponentSymbol, Map<Symbol, Symbol> winsAgainstSymbol) {
            return winsAgainstSymbol.get(this) == opponentSymbol;
        }

        public int getScore(Symbol opponentSymbol, Map<Symbol, Symbol> winsAgainstSymbol) {
            if (this == opponentSymbol) {
                return Result.DRAW.score;
            } else if (winsAgainst(opponentSymbol, winsAgainstSymbol)) {
                return Result.PLAYER_WINS.score;
            } else {
                return Result.PLAYER_LOSES.score;
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

        public int calculateScore(Map<Symbol, Symbol> winsAgainstSymbol) {
            return playerSymbol.score + playerSymbol.getScore(opponentSymbol, winsAgainstSymbol);
        }
    }
}
