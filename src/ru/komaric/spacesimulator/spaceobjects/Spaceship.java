package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public class Spaceship extends MovableSpaceObject {

    private Vector acceleration;

    public Spaceship(String name, Vector radiusVector, double weight, double radius, Vector speed, Vector acceleration) {
        super(name, radiusVector, weight, radius, speed);
        if (acceleration == null) {
            throw new IllegalArgumentException("\"acceleration\" can't be null");
        }
        this.acceleration = acceleration;
    }

    @Override
    public SpaceObject copy() {
        return new Spaceship(name, radiusVector, weight, radius, speed, acceleration);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }
}
