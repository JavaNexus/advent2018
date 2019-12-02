package pl.javanexus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class InputReader {

    public int[] readIntArray(String fileName) throws IOException {
        try (BufferedReader reader = getReader(fileName)) {
            String line = reader.readLine();
            if (line != null) {
                return Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            }
        }

        throw new RuntimeException("File is empty");
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
        return new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)));
    }
}
