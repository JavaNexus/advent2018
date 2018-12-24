package pl.javanexus.day6;

import java.util.*;

public class JarvisAlgorithm implements ConvexHull {

    @Override
    public List<Point> find(List<Point> points) {
        final Point startingPoint = findPointWithSmallestX(points);

        final List<Point> convexHull = new LinkedList<>();
        convexHull.add(new Point(-1, startingPoint.getX(), 0));
        convexHull.add(startingPoint);

        boolean hasReachedStartingPoint = false;
        while(!hasReachedStartingPoint) {
            final Point p1 = convexHull.get(convexHull.size() - 2);
            final Point p2 = convexHull.get(convexHull.size() - 1);
            final Point point = points.stream()
                    .filter(p -> p.getId() != p1.getId() && p.getId() != p2.getId())
                    .max(Comparator.comparingDouble(p -> new Vector(p1, p2).getCosin(new Vector(p2, p))))
                    .get();
            if (point.getId() == startingPoint.getId()) {
                hasReachedStartingPoint = true;
            } else {
                convexHull.add(point);
            }
        }

        return convexHull.subList(1, convexHull.size());
    }

    private Point findPointWithSmallestX(List<Point> points) {
        return points.stream().min(Comparator.comparingInt(Point::getX)).get();
    }
}
