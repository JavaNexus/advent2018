package pl.javanexus.year2018.day6;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CoordinatesTest {

    public static final Point[] POINTS = {
            new Point(1, 1, 1),
            new Point(2, 1, 6),
            new Point(3, 8, 3),
            new Point(4,3, 4),
            new Point(5,5, 5),
            new Point(6,8, 9)
    };
    public static final Point[] POINTS_A_D = {
            new Point(1, 1, 1),
            new Point(4,3, 4),
    };
    public static final String POINTS_FILE_NAME = "day6_coordinates.input";
    public static final String FILE_LINE_PATTERN = "([0-9]+), ([0-9]+)";

    @Test
    public void testFindConvexHull() {
        ConvexHull convexHull = new JarvisAlgorithm();
        List<Point> points = convexHull.find(Arrays.stream(POINTS).collect(Collectors.toList()));

        System.out.println(points);
    }

    @Test
    public void testFindConvexHullFromFile() throws IOException {
        List<Point> points = getPointsFromFile(POINTS_FILE_NAME);

        ConvexHull convexHull = new JarvisAlgorithm();
        List<Point> convexHullPoints = convexHull.find(points);

        System.out.println(convexHullPoints);
    }

    @Test
    public void testGetLargestArea() {
        List<Point> pointsList = Arrays.stream(POINTS).collect(Collectors.toList());

        ConvexHull convexHull = new JarvisAlgorithm();
        Map<Integer, Point> convexHullPoints = convexHull.find(pointsList).stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p));

        Coordinates coordinates = new Coordinates();
        coordinates.setPoints(POINTS);
        coordinates.calculateClosestPoint();
        //coordinates.printGrid();

        assertEquals(16, coordinates.calculateSizeOfRegionWithinMaxDistanceToEachPoint(32));

        coordinates.getAreas().entrySet().stream()
                .filter(e -> !convexHullPoints.containsKey(e.getKey()))
                .sorted(Comparator.comparing(p -> p.getValue().getArea(), Comparator.reverseOrder()))
                .forEach(p -> System.out.println(p));
    }

    /**
     * Wrong:
     * 4=Point(id=4, x=128, y=302, area=39775)  //your answer is too high
     * 11=Point(id=11, x=160, y=69, area=6754)
     *
     * @throws IOException
     */
    @Test
    public void testGetLargestAreaFromFile() throws IOException {
        List<Point> points = getPointsFromFile(POINTS_FILE_NAME);

        ConvexHull convexHull = new JarvisAlgorithm();
        Map<Integer, Point> convexHullPoints = convexHull.find(points).stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p));

        Coordinates coordinates = new Coordinates();
        coordinates.setPoints(points.toArray(new Point[points.size()]));
        coordinates.calculateClosestPoint();

        Map<Integer, Point> pointsWithArea = coordinates.getAreas();

        //"{x:%d, y:%d, id:%d, area:%d},\n"
//        pointsWithArea.values().stream()
//                .forEach(p -> System.out.printf(
//                        "%d,[%d; %d],%d,\n",
//                        p.getId(), p.getX(), p.getY(), p.getArea()));

//        coordinates.printGridToFile();

        pointsWithArea.entrySet().stream()
                .filter(e -> !convexHullPoints.containsKey(e.getKey()))
                .sorted(Comparator.comparing(p -> p.getValue().getArea(), Comparator.reverseOrder()))
                .forEach(p -> System.out.println(p));

        System.out.println("Area size: " + coordinates.calculateSizeOfRegionWithinMaxDistanceToEachPoint(10000));
    }

    private List<Point> getPointsFromFile(String fileName) throws IOException {
        Pattern pattern = Pattern.compile(FILE_LINE_PATTERN);
        return new InputReader().readValues(
                fileName,
                (index, line) -> {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return new Point(index,
                                Integer.parseInt(matcher.group(1)),
                                Integer.parseInt(matcher.group(2)));
                    } else {
                        throw new IllegalArgumentException("Unexpected line: " + line);
                    }
                });
    }
}
