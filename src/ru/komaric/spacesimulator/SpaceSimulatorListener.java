package ru.komaric.spacesimulator;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import java.util.HashMap;

public interface SpaceSimulatorListener {
    void onIteration(HashMap<String, SpaceObject> spaceObjects);
}
