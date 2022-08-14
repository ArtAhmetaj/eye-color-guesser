package models;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Shape {
    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private final double x;
    private final double y;
    private final double radius;


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double[] getCenter() {
        return new double[]{x, y};
    }

    @Override
    public String toString() {
        return "Circle{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }

    @Override
    public List<int[]> getShapeBounds() {
        List<double[]> shapeEdges = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            shapeEdges.add(calculateDistanceOfCenterToRadius(direction));
        }

        var horizontalBounds = new int[]{-1, -1};
        var verticalBounds = new int[]{-1, -1};
        for (var edge : shapeEdges) {
            if (edge[0] > horizontalBounds[0]) horizontalBounds[0] = (int) edge[0];
            if (edge[0] < horizontalBounds[1]) horizontalBounds[1] = (int) edge[0];
            if (edge[1] > verticalBounds[0]) verticalBounds[0] = (int) edge[1];
            if (edge[1] < verticalBounds[1]) verticalBounds[1] = (int) edge[1];
        }

        return List.of(horizontalBounds,verticalBounds);
    }

    private double[] calculateDistanceOfCenterToRadius(Direction direction) {
        var center = getCenter();
        switch (direction) {

            case LEFT:
                center[0] -= radius;
                return center;
            case RIGHT:
                center[0] += radius;
            case UP:
                center[1] += radius;
                return center;
            case DOWN:
                center[1] -= radius;
                return center;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }


    }
}
