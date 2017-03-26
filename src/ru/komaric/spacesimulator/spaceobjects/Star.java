package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Vector;

public class Star extends SpaceObject {

    public Star(String name, Vector radiusVector, double weight, double radius) {
        super(name, radiusVector, weight, radius);
    }

    @Override
    public SpaceObject copy() {
        return new Star(name, radiusVector, weight, radius);
    }
}
