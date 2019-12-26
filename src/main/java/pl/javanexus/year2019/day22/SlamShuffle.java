package pl.javanexus.year2019.day22;

import lombok.Data;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlamShuffle {

    enum Technique {
        REVERSE("deal into new stack") {
            @Override
            public int[] shuffle(int[] deck, int param) {
                int[] shuffledDeck = new int[deck.length];
                for (int i = 0; i < deck.length; i++) {
                    shuffledDeck[i] = deck[deck.length - i - 1];
                }

                return shuffledDeck;
            }
        },
        CUT("cut [-0-9]+") {
            @Override
            public int[] shuffle(int[] deck, int numberOfCards) {
                int[] shuffledDeck = new int[deck.length];
                if (numberOfCards > 0) {
                    System.arraycopy(deck, 0, shuffledDeck, shuffledDeck.length - numberOfCards, numberOfCards);
                    System.arraycopy(deck, numberOfCards, shuffledDeck, 0, shuffledDeck.length - numberOfCards);
                } else {
                    System.arraycopy(deck, deck.length + numberOfCards, shuffledDeck, 0, Math.abs(numberOfCards));
                    System.arraycopy(deck, 0, shuffledDeck, Math.abs(numberOfCards), deck.length + numberOfCards);
                    //Math.abs(numberOfCards)
                }

                return shuffledDeck;
            }
        },
        INCREMENT("deal with increment [0-9]+") {
            @Override
            public int[] shuffle(int[] deck, int param) {
                int[] shuffledDeck = new int[deck.length];
                for (int oldIndex = 0, newIndex = 0; oldIndex < deck.length;
                     oldIndex++, newIndex = (newIndex + param) % deck.length) {
                    shuffledDeck[newIndex] = deck[oldIndex];
                }

                return shuffledDeck;
            }
        };

        public static Instruction parseInstruction(String instruction) {
            for (Technique technique : Technique.values()) {
                Matcher matcher = technique.getMatcher(instruction);
                if (matcher.find()) {
                    return new Instruction(technique, getParam(matcher));
                }
            }

            throw new RuntimeException("Unknown instruction: " + instruction);
        }

        private static int getParam(Matcher matcher) {
            return matcher.groupCount() > 2 ? Integer.parseInt(matcher.group(1)) : -1;
        }

        private final Pattern pattern;

        Technique(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public abstract int[] shuffle(int[] deck, int param);

        private Matcher getMatcher(String instrucion) {
            return pattern.matcher(instrucion);
        }
    }

    public int[] shuffleDeck(List<String> instructions) {
        int[] deck = createDeck(10007);

        for (String instruction : instructions) {
            Technique.parseInstruction(instruction);
        }

        return null;
    }

    private int[] createDeck(int size) {
        int[] deck = new int[size];
        for (int i = 0; i < deck.length; i++) {
            deck[i] = i;
        }

        return deck;
    }

    @Data
    private static class Instruction {

        private final Technique technique;
        private final int param;

        public int[] shuffle(int[] deck) {
            return technique.shuffle(deck, param);
        }
    }
}
