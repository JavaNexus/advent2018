package pl.javanexus.year2020.day2;

import lombok.Value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordPhilosophy {

    private final Pattern pattern = Pattern.compile("([0-9]+)-([0-9]+) ([a-zA-Z]): ([a-zA-Z]+)");//1-3 a: abcde

    public int countValidPasswordsWithOldPolicy(List<String> lines) {
        return countValidPasswords(lines, new OldPolicy());
    }

    public int countValidPasswordsWithNewPolicy(List<String> lines) {
        return countValidPasswords(lines, new NewPolicy());
    }

    private int countValidPasswords(List<String> lines, Policy policy) {
        int validPasswords = 0;
        for (String line : lines) {
            if (policy.isValid(parseLine(line))) {
                validPasswords++;
            }
        }

        return validPasswords;
    }

    private PasswordRecord parseLine(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new PasswordRecord(
                    Integer.parseInt(matcher.group(1)),//min
                    Integer.parseInt(matcher.group(2)),//max
                    matcher.group(3).charAt(0),//letter
                    matcher.group(4));//password
        } else {
            throw new IllegalArgumentException("Unexpected line format: " + line);
        }
    }

    @Value
    private class PasswordRecord {
        int min;
        int max;
        char letter;
        String password;
    }

    interface Policy {
        boolean isValid(PasswordRecord record);
    }

    class OldPolicy implements Policy {

        @Override
        public boolean isValid(PasswordRecord record) {
            int count = 0;
            for (int i = 0; i < record.getPassword().length(); i++) {
                if (record.getPassword().charAt(i) == record.getLetter()) {
                    count++;
                }
            }

            return count >= record.getMin() && count <= record.getMax();
        }
    }

    class NewPolicy implements Policy {

        @Override
        public boolean isValid(PasswordRecord record) {
            String password = record.getPassword();
            return password.charAt(record.getMin() - 1) == record.getLetter()
                    ^ password.charAt(record.getMax() - 1) == record.getLetter();
        }
    }
}
