package pl.javanexus.day15;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BattleTest {

    private BoardParser boardParser;

    @Before
    public void setUp() throws Exception {
        this.boardParser = new BoardParser();
    }

    @Test
    public void testGetResult() throws Exception {
        boardParser.parseInput("day15_simple.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        battle.printMap();

//        assertEquals(-1, battle.getResult());
    }
}
