package pl.javanexus.year2018.day14;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ScoreboardTest {

    public static final byte[] INITIAL_POINTS = {3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9};
    public static final int FIRST_ELF_INDEX = 1;//0;//
    public static final int SECOND_ELF_INDEX = 13;//6;//
    public static final int INPUT = 909441;

    private ScoreboardByteList getScoreboardByteList() {
        return new ScoreboardByteList(INITIAL_POINTS, FIRST_ELF_INDEX, SECOND_ELF_INDEX);
    }

    private ScoreboardByteBuffer getScoreboardByteBuffer() {
        return new ScoreboardByteBuffer(INITIAL_POINTS, FIRST_ELF_INDEX, SECOND_ELF_INDEX);
    }

    @Test
    public void calculatePoints() {
        ScoreboardByteList scoreboard = getScoreboardByteList();
        testCalculatePoints(scoreboard, new byte[] {1, 0}, (byte)3, (byte)7);
        testCalculatePoints(scoreboard, new byte[] {1}, (byte)1, (byte)0);
        testCalculatePoints(scoreboard, new byte[] {5}, (byte)1, (byte)4);
    }

    @Test
    public void testFoo() {
        getScoreboardByteList().getScore(512);
    }

    @Test
    public void testFindRecipe() {
//        assertEquals(5, getScoreboardByteBuffer().findRecipe("01245"));
        assertEquals(9, getScoreboardByteBuffer().findRecipe("51589"));
        assertEquals(18, getScoreboardByteBuffer().findRecipe("92510"));
        assertEquals(2018, getScoreboardByteBuffer().findRecipe("59414"));

        getScoreboardByteBuffer().saveRecipes();

//        System.out.println(getScoreboardByteBuffer().findRecipe(Integer.toString(INPUT)));
        //1) 256024434 //That's not the right answer; your answer is too high.
        //java.nio.HeapByteBuffer[pos=256024440 lim=1073741824 cap=1073741824]

        //2) 20403321  //That's not the right answer; your answer is too high.
        //java.nio.HeapByteBuffer[pos=20403327 lim=134217728 cap=134217728]

        //3) 20403320
        //4) 20403321
    }

    @Test
    public void testGetScore() {
        assertEquals("5158916779", getScoreboardByteBuffer().getScore(9));
        assertEquals("0124515891", getScoreboardByteBuffer().getScore(5));
        assertEquals("9251071085", getScoreboardByteBuffer().getScore(18));
        assertEquals("5941429882", getScoreboardByteBuffer().getScore(2018));

        System.out.println(getScoreboardByteBuffer().getScore(INPUT));
    }

    private void testCalculatePoints(ScoreboardByteList scoreboardByteList, byte[] expected, byte x, byte y) {
        Assert.assertArrayEquals(expected, scoreboardByteList.calculatePoints(x, y));
    }

    @Test
    public void testBinary() {
        int numberOfDigits = Integer.toString(INPUT).length();

        ByteBuffer[] buffers = new ByteBuffer[numberOfDigits];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = ByteBuffer.allocate(numberOfDigits);
        }

        byte[][] nextValues = {
                new byte[] {5},
                new byte[] {1},
                new byte[] {5},
                new byte[] {8},
                new byte[] {9},
                new byte[] {1, 6},
                new byte[] {7},
                new byte[] {7},
                new byte[] {9},
                new byte[] {2},
        };

        ByteBuffer buffer = buffers[0];
        for (byte[] value : nextValues) {
            buffer.put(value[0]);
            if (buffer.position() == buffer.capacity()) {
                buffer.rewind();
            }
        }
    }

    private void appendDigits(BigInteger number, byte[] digits) {

    }
}
