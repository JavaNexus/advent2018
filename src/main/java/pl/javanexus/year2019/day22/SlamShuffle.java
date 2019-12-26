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

            @Override
            public int getNextIndex(int oldIndex, int deckSize, int param) {
                return deckSize - oldIndex - 1;
            }
        },
        CUT("cut ([-0-9]+)") {
            @Override
            public int[] shuffle(int[] deck, int cutSize) {
                int[] shuffledDeck = new int[deck.length];
                if (cutSize < 0) {
                    cutSize = deck.length + cutSize;
                }
                System.arraycopy(deck, 0, shuffledDeck, shuffledDeck.length - cutSize, cutSize);
                System.arraycopy(deck, cutSize, shuffledDeck, 0, shuffledDeck.length - cutSize);

                return shuffledDeck;
            }

            @Override
            public int getNextIndex(int oldIndex, int deckSize, int cutSize) {
                if (cutSize < 0) {
                    cutSize = deckSize + cutSize;
                }

                return oldIndex < cutSize ? deckSize - (cutSize - oldIndex) : oldIndex - cutSize;
            }
        },
        INCREMENT("deal with increment ([0-9]+)") {
            @Override
            public int[] shuffle(int[] deck, int param) {
                int[] shuffledDeck = new int[deck.length];
                for (int oldIndex = 0, newIndex = 0; oldIndex < deck.length;
                     oldIndex++, newIndex = (newIndex + param) % deck.length) {
                    shuffledDeck[newIndex] = deck[oldIndex];
                }

                return shuffledDeck;
            }

            @Override
            public int getNextIndex(int oldIndex, int deckSize, int increment) {
                return (oldIndex * increment) % deckSize;
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
            return matcher.groupCount() > 0 ? Integer.parseInt(matcher.group(1)) : -1;
        }

        private final Pattern pattern;

        Technique(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public abstract int[] shuffle(int[] deck, int param);

        public abstract int getNextIndex(int oldIndex, int deckSize, int param);

        private Matcher getMatcher(String instrucion) {
            return pattern.matcher(instrucion);
        }
    }

    public int[] shuffleDeck(List<String> instructions, int deckSize) {
        int[] deck = createDeck(deckSize);

        for (String instruction : instructions) {
            System.out.println(instruction);
            deck = Technique.parseInstruction(instruction).shuffle(deck);
        }

        return deck;
    }

    public int findIndex(int[] deck, int value) {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] == value) {
                return i;
            }
        }
        throw new RuntimeException("Value: " + value + " not found in deck");
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
