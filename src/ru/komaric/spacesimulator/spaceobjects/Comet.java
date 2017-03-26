package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public class Comet extends MovableSpaceObject implements Fadable {

    public Comet(String name, Vector radiusVector, double weight, double radius, Vector speed) {
        super(name, radiusVector, weight, radius, speed);
    }

    @Override
    public SpaceObject copy() {
        return new Comet(name, radiusVector, weight, radius, speed);
    }

    @Override
    public boolean isDestroyed() {
        return radius == 0;
    }
}
