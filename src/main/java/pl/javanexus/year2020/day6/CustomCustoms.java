package pl.javanexus.year2020.day6;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomCustoms {

    public int sumUniqueAnswers(List<String> lines) {
        int uniqueAnswers = 0;

        Set<Character> questions = new HashSet<>();
        for (String line : lines) {
            if (Strings.isNullOrEmpty(line)) {
                uniqueAnswers += questions.size();
                questions = new HashSet<>();
            }
            parseForm(line, questions);
        }
        uniqueAnswers += questions.size();

        return uniqueAnswers;
    }

    public int sumCommonAnswers(List<String> lines) {
        int commonAnswers = 0;

        Group group = new Group();
        for (String line : lines) {
            if (Strings.isNullOrEmpty(line)) {
                commonAnswers += group.countCommonAnswers();
                group = new Group();
            } else {
                group.addForm(line);
            }
        }
        commonAnswers += group.countCommonAnswers();

        return commonAnswers;
    }

    public void parseForm(String line, Set<Character> questions) {
        for (char c : line.toCharArray()) {
            questions.add(c);
        }
    }

    class Group {
        private int size = 0;
        private Map<Character, Integer> numberOfAnswers = new HashMap<>();

        public void addForm(String line) {
            size++;
            for (char c : line.toCharArray()) {
                numberOfAnswers.compute(c, (question, oldNumber) -> oldNumber == null ? 1 : oldNumber + 1);
            }
        }

        public long countCommonAnswers() {
            return numberOfAnswers.values().stream()
                    .mapToInt(Integer::intValue)
                    .filter(number -> number == size)
                    .count();
        }
    }
}
