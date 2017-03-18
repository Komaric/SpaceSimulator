package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Point;

public class Star extends SpaceObject {

    public Star(String name, Point coordinates, double weight, double radius) {
        super(name, coordinates, weight, radius);
    }

    @Override
    public SpaceObject copy() {
        return new Star(name, coordinates, weight, radius);
    }
}
