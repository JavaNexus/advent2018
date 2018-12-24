package pl.javanexus.day15;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BattleTest {

    private BoardParser boardParser;

    @Before
    public void setUp() throws Exception {
        this.boardParser = new BoardParser();
    }

    @Test
    public void testGetResult() throws Exception {
        boardParser.parseInput("day15_path.input");//simple

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        battle.printMap();
        battle.calculateDistance(
                battle.getTile(1, 1),
                Arrays.asList(
                        battle.getTile(3, 1),
                        battle.getTile(5, 1),
                        battle.getTile(2, 2),
                        battle.getTile(5, 2),
                        battle.getTile(1, 3),
                        battle.getTile(3, 3)
                ));

//        assertEquals(-1, battle.getResult());
    }
}
