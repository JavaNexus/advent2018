package pl.javanexus.day6;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class GrahamAlgorithm implements ConvexHull {

    @Override
    public List<Point> find(List<Point> points) {
        Point startingPoint = findPointWithSmallestY(points);
        List<Point> pointsSortedByPlanarAngle = points.stream()
                .filter(point -> point.getId() != startingPoint.getId())
                .sorted(Comparator.comparingDouble(p -> p.getCosin(startingPoint)))
                .collect(Collectors.toList());

        Stack<Point> stack = new Stack<>();
        stack.push(startingPoint);
        stack.push(pointsSortedByPlanarAngle.get(0));

        for (int i = 2; i < pointsSortedByPlanarAngle.size(); i++) {
            while (stack.size() >= 2 && ccw(stack.get(0), stack.get(1), pointsSortedByPlanarAngle.get(i)) <= 0) {
                stack.pop();
            }
            stack.push(pointsSortedByPlanarAngle.get(i));
        }

        return stack;
    }

    private Point findPointWithSmallestY(List<Point> points) {
        return points.stream().min(Comparator.comparingInt(Point::getY)).get();
    }

    private int ccw(Point p1, Point p2, Point p3) {
        return (p2.getX() - p1.getX())*(p3.getY() - p1.getY()) - (p2.getY() - p1.getY())*(p3.getX() - p1.getX());
    }
}
