package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public abstract class MovableSpaceObject extends SpaceObject {

    protected Vector speed;

    public MovableSpaceObject(String name, Vector radiusVector, double weight, double radius, Vector speed) {
        super(name, radiusVector, weight, radius);
        if (speed == null) {
            throw new IllegalArgumentException("\"speed\" can't be null");
        }
        this.speed = speed;
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        if (speed == null) {
            throw new IllegalArgumentException("\"speed\" can't be null");
        }
        this.speed = speed;
    }
}

