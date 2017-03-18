package ru.komaric.spacesimulator.util;

public class Vector {

    private Point direction;
    private double value;

    public Vector(Point direction, double value) {
        if (direction == null) {
            throw new IllegalArgumentException("\"direction\" can't be null");
        }
        this.direction = direction;
        this.value = value;
    }

    public Vector(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("\"vector\" can't be null");
        }
        this.direction = vector.direction;
        this.value = vector.value;
    }

    public Point getDirection() {
        return direction;
    }

    public double getValue() {
        return value;
    }

    public Vector normalize(Vector vector) {
        double x = vector.direction.getX();
        double y = vector.direction.getY();
        double length = Math.sqrt(x * x + y * y);
        Point direction = new Point(x / length, y / length);
        return new Vector(direction, this.value);
    }
}
