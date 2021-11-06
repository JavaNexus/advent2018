package pl.javanexus.year2020.day4;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PassportProcessingTest {

    private final PassportParser passportParser = new PassportParser();

    @Test
    public void shouldCountValidPassports() throws IOException {
        List<String> requiredFieldNames = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");


        PassportProcessing passportProcessing = new PassportProcessing(requiredFieldNames);
        assertEquals(2,
                passportProcessing.countValidPassports(getPassports("year2020/day4/input1.txt")));
        assertEquals(260,
                passportProcessing.countValidPassports(getPassports("year2020/day4/input2.txt")));
    }

    private List<Passport> getPassports(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        return passportParser.readPassports(inputStream);
    }

}
