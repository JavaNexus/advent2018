package pl.javanexus.year2018.day7;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SleighKit {

    private static final Pattern LINE_PATTERN =
            Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");

    private final Map<String, Vertex> vertices = new HashMap<>();
    private final int baseStepTime;
    private final List<Worker> workers;

    public SleighKit(int baseStepTime, int numberOfWorkers, String[] steps) {
        this.baseStepTime = baseStepTime;
        this.workers = IntStream.range(0, numberOfWorkers)
                .mapToObj(id -> new Worker())
                .collect(Collectors.toList());
        createVertices(steps);
    }

    private void createVertices(String[] steps) {
        for (String step : steps) {
            Matcher matcher = LINE_PATTERN.matcher(step);
            if (matcher.find()) {
                String fromId = matcher.group(1);
                String toId = matcher.group(2);

                Vertex from = getVertex(fromId);
                Vertex to = getVertex(toId);

                from.getPost().add(to);
                to.getPre().add(from);
            }
        }
    }

    private Vertex getVertex(String vertexId) {
        Vertex vertex = vertices.get(vertexId);
        if (vertex == null) {
            vertex = new Vertex(vertexId, baseStepTime);
            vertices.put(vertexId, vertex);
        }

        return vertex;
    }

    public AssembledSleigh assemble() {
        int currentTime = 0;

        final Set<Vertex> verticesToConsider = new HashSet<>();
        vertices.values().stream()
                .filter(v -> v.getPre().isEmpty())
                .forEach(vertex -> verticesToConsider.add(vertex));

        final StringBuilder builder = new StringBuilder();
        while (builder.length() < vertices.size()) {
            workers.stream().forEach(Worker::updateTimeSpent);
            workers.stream().filter(Worker::isFinished).forEach(worker -> {
                Vertex finishedTask = worker.getCurrentTask();
                if (finishedTask != null) {
                    verticesToConsider.addAll(finishedTask.getPost());
                    builder.append(finishedTask.getName());
                    worker.setCurrentTask(null);
                }

                verticesToConsider.stream()
                        .filter(v -> v.isReady(builder.toString()))
                        .sorted(Comparator.comparing(Vertex::getName))
                        .findFirst()
                        .ifPresent(v -> {
                            worker.setCurrentTask(v);
                            verticesToConsider.remove(v);
                        });
            });



            currentTime++;
        }

        return new AssembledSleigh(builder.toString(), currentTime);
    }
}
