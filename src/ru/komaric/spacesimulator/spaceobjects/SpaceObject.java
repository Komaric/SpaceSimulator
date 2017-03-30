package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public abstract class SpaceObject {

    protected String name;
    protected Vector radiusVector;
    protected double weight;
    protected double radius;

    public SpaceObject(String name, Vector radiusVector, double weight, double radius) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("\"name\" can't be null or empty");
        }
        if (radiusVector == null) {
            throw new IllegalArgumentException("\"radiusVector\" can't be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("\"weight\" must be positive");
        }
        if (radius < 0) {
            throw new IllegalArgumentException("\"radius\" must be non-negative");
        }
        this.name = name;
        this.radiusVector = radiusVector;
        this.weight = weight;
        this.radius = radius;
    }

    public abstract SpaceObject copy();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("\"name\" can't be null or empty");
        }
        this.name = name;
    }

    public Vector getRadiusVector() {
        return radiusVector;
    }

    public void setRadiusVector(Vector radiusVector) {
        if (radiusVector == null) {
            throw new IllegalArgumentException("\"radiusVector\" can't be null");
        }
        this.radiusVector = radiusVector;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("\"weight\" must be positive");
        }
        this.weight = weight;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("\"radius\" must be non-negative");
        }
        this.radius = radius;
    }

    public double distance(SpaceObject spaceObject) {
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"spaceObject\" can't be null");
        }
        return spaceObject.getRadiusVector().sub(radiusVector).getLength();
    }

    public boolean isOverlapped(SpaceObject spaceObject) {
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"spaceObject\" can't be null");
        }
        return (this.radius + spaceObject.radius) > this.distance(spaceObject);
    }
}
