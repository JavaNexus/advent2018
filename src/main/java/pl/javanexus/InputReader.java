package pl.javanexus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class InputReader {

    public long[] readLongArray(String fileName, String delimiter) throws IOException {
        return getValuesStream(fileName, delimiter).mapToLong(Long::parseLong).toArray();
    }

    public int[] readIntArray(String fileName, String delimiter) throws IOException {
        return getValuesStream(fileName, delimiter).mapToInt(Integer::parseInt).toArray();
    }

    private Stream<String> getValuesStream(String fileName, String delimiter) throws IOException {
        try (BufferedReader reader = getReader(fileName)) {
            String line = reader.readLine();
            if (line != null) {
                return Arrays.stream(line.split(delimiter));
            }
        }

        throw new RuntimeException("File is empty");
    }

    public Stream<String> getLinesStream(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Couldn't find file: " + fileName);
        }
        return new BufferedReader(new InputStreamReader(inputStream)).lines();
    }

    public List<String> readStringValues(String fileName) throws IOException {
        return readValues(fileName, (index, value) -> value);
    }

    public List<Integer> readIntegerValues(String fileName) throws IOException {
        return readValues(fileName, (index, value) -> Integer.parseInt(value));
    }

    public <T> List<T> readValues(String fileName, BiFunction<Integer, String, T> parser) throws IOException {
        List<T> values = new LinkedList<>();
        try (BufferedReader reader = getReader(fileName)) {
            int index = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(parser.apply(index++, line));
            }
        }

        return values;
    }

    private BufferedReader getReader(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Couldn't find file: " + fileName);
        }

        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
