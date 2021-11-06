package pl.javanexus.year2020.day4;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PassportProcessing {

    private final List<String> requiredFieldNames;

    private final Pattern heightPattern = Pattern.compile("([0-9]+)(cm|in)");

    private final Map<String, Function<String, Boolean>> validators = Map.of(
            //byr (Birth Year) - four digits; at least 1920 and at most 2002.
            "byr", new YearValidator(1920, 2002),
            //iyr (Issue Year) - four digits; at least 2010 and at most 2020.
            "iyr", new YearValidator(2010, 2020),
            //eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
            "eyr", new YearValidator(2020, 2030),
            //hgt (Height) - a number followed by either cm or in:
            //If cm, the number must be at least 150 and at most 193.
            //If in, the number must be at least 59 and at most 76.
            "hgt", (value) -> {
                Matcher matcher = heightPattern.matcher(value);
                if (matcher.find()) {
                    int height = Integer.parseInt(matcher.group(1));
                    switch (matcher.group(2)) {
                        case "cm":
                            return height >= 150 && height <= 193;
                        case "in":
                            return height >= 59 && height <= 76;
                        default:
                            return false;
                    }
                }
                return false;
            },
            //hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            "hcl", (value) -> value.matches("#[0-9a-f]{6}"),
            //ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            "ecl", (value) -> value.matches("(amb|blu|brn|gry|grn|hzl|oth)"),
            //pid (Passport ID) - a nine-digit number, including leading zeroes.
            "pid", (value) -> value.matches("[0-9]{9}"),
            //cid (Country ID) - ignored, missing or not.
            "cid", (value) -> true
    );

    @RequiredArgsConstructor
    class YearValidator implements Function<String, Boolean> {

        private final int min, max;

        @Override
        public Boolean apply(String value) {
            if (value.matches("[0-9]{4}")) {
                int year = Integer.parseInt(value);
                return year >= min && year <= max;
            }
            return false;
        }
    }

    public int countValidPassports(List<Passport> passports) {
        int validPassports = 0;
        for (Passport passport : passports) {
            if (passport.hasFields(requiredFieldNames) && areAllFieldsValid(passport.getFields())) {
                validPassports++;
            }
        }

        return validPassports;
    }

    private boolean areAllFieldsValid(Map<String, String> fields) {
        boolean isValid = true;
        for (Map.Entry<String, String> field : fields.entrySet()) {
            isValid &= validators.getOrDefault(field.getKey(), (value) -> false).apply(field.getValue());
        }

        return isValid;
    }
}
