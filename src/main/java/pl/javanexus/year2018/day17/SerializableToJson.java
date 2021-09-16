package pl.javanexus.year2018.day17;

import java.util.Collection;
import java.util.stream.Collectors;

public interface SerializableToJson {

    String ARRAY_DELIMITER = ", ";
    String ARRAY_PREFIX = "[";
    String ARRAY_SUFFIX = "]";

    String toJSON();
    default <T extends SerializableToJson> String toJSON(Collection<T> values) {
        return values.stream()
                .map(SerializableToJson::toJSON)
                .collect(Collectors.joining(ARRAY_DELIMITER, ARRAY_PREFIX, ARRAY_SUFFIX));
    }
}
