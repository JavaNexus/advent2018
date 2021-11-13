package pl.javanexus.year2020.day7;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HandyHaversacksTest {

    private RulesParser bagParser;
    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        bagParser = new RulesParser();
        inputReader = new InputReader();
    }

    @Test
    public void shouldCountParentBags() throws IOException {
        assertEquals(4,
                getHandyHaversacks("year2020/day7/input1.txt").getParentBags("shiny gold").size());
        assertEquals(185,
                getHandyHaversacks("year2020/day7/input2.txt").getParentBags("shiny gold").size());
    }

    @Test
    public void shouldCountChildrenBags() throws IOException {
        assertEquals(32,
                getHandyHaversacks("year2020/day7/input1.txt").countChildrenBags("shiny gold"));
        assertEquals(89084,
                getHandyHaversacks("year2020/day7/input2.txt").countChildrenBags("shiny gold"));
    }

    private HandyHaversacks getHandyHaversacks(String fileName) throws IOException {
        Map<String, Bag> bags = bagParser.parseRules(inputReader.readStringValues(fileName));
        return new HandyHaversacks(bags);
    }
}
