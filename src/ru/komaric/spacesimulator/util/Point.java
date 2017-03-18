package ru.komaric.spacesimulator.util;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("\"point\" can't be null");
        }
        this.x = point.x;
        this.y = point.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("\"point\" can't be null");
        }
        double dx = this.x - point.x;
        double dy = this.y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
