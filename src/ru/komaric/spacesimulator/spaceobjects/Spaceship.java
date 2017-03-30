package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public class Spaceship extends MovableSpaceObject {

    private Vector acceleration;

    public Spaceship(String name, Vector radiusVector, double weight, Vector speed, Vector acceleration) {
        super(name, radiusVector, weight, 0, speed);
        if (acceleration == null) {
            throw new IllegalArgumentException("\"acceleration\" can't be null");
        }
        this.acceleration = acceleration;
    }

    @Override
    public void setRadius(double radius) {
        throw new RuntimeException("Spaceship always has zero weight");
    }

    @Override
    public SpaceObject copy() {
        return new Spaceship(name, radiusVector, weight, speed, acceleration);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        if (acceleration == null) {
            throw new IllegalArgumentException("\"acceleration\" can't be null");
        }
        this.acceleration = acceleration;
    }
}
