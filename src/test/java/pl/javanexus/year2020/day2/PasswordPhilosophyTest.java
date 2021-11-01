package pl.javanexus.year2020.day2;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PasswordPhilosophyTest {

    @Test
    public void shouldCountValidPasswords() {
        List<String> lines = List.of(
                "1-3 a: abcde",
                "1-3 b: cdefg",
                "2-9 c: ccccccccc"
        );
        PasswordPhilosophy passwordPhilosophy = new PasswordPhilosophy();
        assertEquals(2, passwordPhilosophy.countValidPasswordsWithOldPolicy(lines));
        assertEquals(1, passwordPhilosophy.countValidPasswordsWithNewPolicy(lines));
    }

    @Test
    public void shouldCountValidPasswordsInFile() throws IOException {
        List<String> lines = new InputReader().readStringValues("year2020/day2/input1.csv");

        PasswordPhilosophy passwordPhilosophy = new PasswordPhilosophy();
        System.out.println(passwordPhilosophy.countValidPasswordsWithOldPolicy(lines));
        System.out.println(passwordPhilosophy.countValidPasswordsWithNewPolicy(lines));
    }
}
