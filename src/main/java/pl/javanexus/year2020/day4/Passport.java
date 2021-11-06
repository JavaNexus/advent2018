package pl.javanexus.year2020.day4;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Passport {

    private final Map<String, String> fields = new HashMap<>();

    public void addField(String name, String value) {
        fields.put(name, value);
    }

    public boolean hasFields(List<String> fieldNames) {
        return fields.keySet().containsAll(fieldNames);
    }

    public Map<String, String> getFields() {
        return Collections.unmodifiableMap(fields);
    }
}
