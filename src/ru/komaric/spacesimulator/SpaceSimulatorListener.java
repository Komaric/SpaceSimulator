package ru.komaric.spacesimulator;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import java.util.Set;

public interface SpaceSimulatorListener {
    void onIteration(Set<SpaceObject> spaceObjects);
}
