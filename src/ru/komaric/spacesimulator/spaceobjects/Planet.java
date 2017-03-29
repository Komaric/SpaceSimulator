package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public class Planet extends MovableSpaceObject {

    public Planet(String name, Vector radiusVector, double weight, double radius, Vector speed) {
        super(name, radiusVector, weight, radius, speed);
    }

    @Override
    public SpaceObject copy() {
        return new Planet(name, radiusVector, weight, radius, speed);
    }
}
