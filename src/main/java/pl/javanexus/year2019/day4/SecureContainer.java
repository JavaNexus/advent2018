package pl.javanexus.year2019.day4;

import java.util.Arrays;

public class SecureContainer {

    public int findNumberOfPasswords(int min, int max) {
        int numberOfPasswords = 0;
        for (int i = min; i < max; i++) {
            if (isValidStrictPassword(getDigits(Integer.toString(i)))) {
                numberOfPasswords++;
            }
        }

        return numberOfPasswords;
    }

    public int[] getDigits(String password) {
        return Arrays.stream(password.split("")).mapToInt(Integer::parseInt).toArray();
    }

    public boolean isValidStrictPassword(int[] password) {
        boolean hasTwoAdjacentMatchingDigits = false;
        int numberOfAdjacentMatchingDigits = 0;
        int previousDigit = -1;
        for (int i = 0; i < password.length; i++) {
            if (password[i] < previousDigit) {
                return false;
            } else if (password[i] == previousDigit) {
                numberOfAdjacentMatchingDigits++;
            } else {
                if (numberOfAdjacentMatchingDigits == 1) {
                    hasTwoAdjacentMatchingDigits = true;
                }
                numberOfAdjacentMatchingDigits = 0;
            }
            previousDigit = password[i];
        }
        if (numberOfAdjacentMatchingDigits == 1) {
            hasTwoAdjacentMatchingDigits = true;
        }

        return hasTwoAdjacentMatchingDigits;
    }

    public boolean isValidPassword(int[] password) {
        boolean hasRepeatedConsecutiveDigit = false;
        int previousDigit = -1;
        for (int i = 0; i < password.length; i++) {
            if (password[i] < previousDigit) {
                return false;
            } else if (password[i] == previousDigit) {
                hasRepeatedConsecutiveDigit = true;
            }
            previousDigit = password[i];
        }

        return hasRepeatedConsecutiveDigit;
    }
}
