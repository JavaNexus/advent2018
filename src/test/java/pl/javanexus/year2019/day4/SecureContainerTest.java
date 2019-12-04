package pl.javanexus.year2019.day4;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecureContainerTest {

    private SecureContainer secureContainer;

    @Before
    public void setUp() throws Exception {
        this.secureContainer = new SecureContainer();
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(secureContainer.isValidPassword(secureContainer.getDigits("111111")));
        assertFalse(secureContainer.isValidPassword(secureContainer.getDigits("223450")));
        assertFalse(secureContainer.isValidPassword(secureContainer.getDigits("123789")));

        assertTrue(secureContainer.isValidStrictPassword(secureContainer.getDigits("112233")));
        assertFalse(secureContainer.isValidStrictPassword(secureContainer.getDigits("123444")));
        assertTrue(secureContainer.isValidStrictPassword(secureContainer.getDigits("111122")));
    }

    @Test
    public void testFindNumberOfPasswords() {
        int numberOfPasswords = secureContainer.findNumberOfPasswords(272091, 815432);
        System.out.println(numberOfPasswords);
    }
}
