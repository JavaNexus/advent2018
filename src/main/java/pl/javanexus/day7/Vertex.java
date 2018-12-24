package pl.javanexus.day7;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Vertex {

    public static final char BASE_STEP_NAME = 'A';

    private final String name;
    private final int baseStepTime;
    private final Set<Vertex> pre = new HashSet<>();
    private final Set<Vertex> post = new HashSet<>();

    public boolean isReady(String used) {
        Set<String> usedNames = Arrays.stream(used.split("")).collect(Collectors.toSet());

        return pre.size() == pre.stream()
                .filter(preVertex -> usedNames.contains(preVertex.getName()))
                .collect(Collectors.toSet()).size();
    }

    public int getTime() {
        return baseStepTime + name.toCharArray()[0] - BASE_STEP_NAME + 1;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vertex vertex = (Vertex) o;

        return name != null ? name.equals(vertex.name) : vertex.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
