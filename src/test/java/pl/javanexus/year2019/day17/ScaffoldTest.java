package pl.javanexus.year2019.day17;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day17.Scaffold;

import java.io.IOException;
import java.util.Arrays;

public class ScaffoldTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testPrintScaffold() throws IOException {
        long[] instructions =
                inputReader.readLongArray("year2019/day17/input1.csv", ",");
        Scaffold scaffold = new Scaffold();
        scaffold.readInput(instructions);
        scaffold.printGrid();

        int intersections = scaffold.countIntersections();
        System.out.println("intersections: " + intersections);
    }

    @Test
    public void testTraverseScaffold() throws IOException {
        long[] instructions =
                inputReader.readLongArray("year2019/day17/input1.csv", ",");
        instructions[0] = 2;

        Scaffold scaffold = new Scaffold();
        scaffold.readInput(instructions);

        //0, 32
    }

    @Test
    public void findLongestPattern() {
        String[] sequence = "R,8,R,8,R,4,R,4,R,8,L,6,L,2,R,4,R,4,R,8,R,8,R,8,L,6,L,2".split(",");
        countWords(sequence, 10);
//        for (int length = sequence.length / 2; length > 1; length--) {
//            for (int offset = 0; offset < length; offset++) {
//                if (sequence[offset].equals(sequence[offset + 1])) {
//                    //...
//                }
//            }
//        }
    }

    /**
     *
     * @param wordLength = 3
     */
    private void countWords(String[] sequence, int wordLength) {
        for (int offset = 0; offset < wordLength; offset++) {
//            System.out.println("Offset: " + offset);
            for (int firstLetterIndex = offset; firstLetterIndex < sequence.length - wordLength; firstLetterIndex += wordLength) {
                String[] pattern = Arrays.copyOfRange(sequence, firstLetterIndex, firstLetterIndex + wordLength);
                System.out.println("Word: " + Arrays.toString(pattern)
                        + " found: " + countWordOccurrence(sequence, pattern) + " times");
            }
        }
    }

    private int countWordOccurrence(String[] sequence, String[] word) {
        int occurrence = 0;

        int subSequenceLength = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i].equals(word[subSequenceLength])) {
                subSequenceLength++;
                if (subSequenceLength == word.length) {
//                    System.out.println("Full word found: " + Arrays.toString(word));
                    subSequenceLength = 0;
                    occurrence++;
                }
            } else {
                subSequenceLength = 0;
            }
        }

        return occurrence;
    }
}
