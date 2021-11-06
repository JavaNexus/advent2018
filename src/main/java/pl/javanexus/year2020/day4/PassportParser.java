package pl.javanexus.year2020.day4;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassportParser {

    private final Pattern pattern = Pattern.compile("(.+):(.+)");

    public List<Passport> readPassports(InputStream inputStream) throws IOException {
        final List<Passport> passports = new LinkedList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        Passport passport = null;
        while ((line = reader.readLine()) != null) {
            if (passport == null || Strings.isNullOrEmpty(line)) {
                passport = new Passport();
                passports.add(passport);
            }
            for (String fieldValue : line.split(" ")) {
                Matcher matcher = pattern.matcher(fieldValue);
                if (matcher.find()) {
                    passport.addField(matcher.group(1), matcher.group(2));
                }
            }
        }

        return passports;
    }
}
