package pl.javanexus.year2020.day4;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PassportProcessing {

    private final List<String> requiredFieldNames;

    public int countValidPassports(List<Passport> passports) {
        int validPassports = 0;
        for (Passport passport : passports) {
            if (passport.hasFields(requiredFieldNames)) {
                validPassports++;
            }
        }

        return validPassports;
    }
}
