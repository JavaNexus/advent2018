package pl.javanexus.year2020.day7;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Collection;
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
        HandyHaversacks handyHaversacks = getHandyHaversacks("year2020/day7/input1.txt");
        Collection<Bag> parentBags = handyHaversacks.getParentBags("shiny gold");
        assertEquals(4, parentBags.size());
    }

    @Test
    public void shouldCountParentBagsFromInput() throws IOException {
        HandyHaversacks handyHaversacks = getHandyHaversacks("year2020/day7/input2.txt");
        Collection<Bag> parentBags = handyHaversacks.getParentBags("shiny gold");
        assertEquals(185, parentBags.size());
    }

    private HandyHaversacks getHandyHaversacks(String fileName) throws IOException {
        Map<String, Bag> bags = bagParser.parseRules(inputReader.readStringValues(fileName));
        return new HandyHaversacks(bags);
    }
}
