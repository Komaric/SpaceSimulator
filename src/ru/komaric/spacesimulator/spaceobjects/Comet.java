package ru.komaric.spacesimulator.spaceobjects;

import ru.komaric.spacesimulator.util.Point;
import ru.komaric.spacesimulator.util.Vector;

public class Comet extends MovableSpaceObject implements Destroyable {

    public Comet(String name, Point coordinates, double weight, double radius, Vector speed) {
        super(name, coordinates, weight, radius, speed);
    }

    @Override
    public SpaceObject copy() {
        return new Comet(name, coordinates, weight, radius, speed);
    }

    @Override
    public boolean isDestroyed() {
        return radius == 0;
    }
}
