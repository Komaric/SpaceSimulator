package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Point;

public abstract class SpaceObject {

    protected String name;
    protected Point coordinates;
    protected double weight;
    protected double radius;

    public SpaceObject(String name, Point coordinates, double weight, double radius) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("\"name\" can't be null or empty");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("\"coordinates\" can't be null");
        }
        this.name = name;
        this.coordinates = coordinates;
        this.weight = weight;
        this.radius = radius;
    }

    public abstract SpaceObject copy();

    public String getName() {
        return name;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public double getWeight() {
        return weight;
    }

    public double getRadius() {
        return radius;
    }

    public double distance(SpaceObject spaceObject) {
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"other\" can't be null");
        }
        return coordinates.distance(spaceObject.coordinates);
    }

    public boolean isOverlapped(SpaceObject spaceObject) {
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"other\" can't be null");
        }
        return (this.radius + spaceObject.radius) > this.distance(spaceObject);
    }
}
