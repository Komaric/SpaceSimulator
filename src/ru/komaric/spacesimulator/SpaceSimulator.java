package ru.komaric.spacesimulator;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

public class SpaceSimulator {

    //period в секундах
    private long period;
    private HashMap<String, SpaceObject> spaceObjects;
    private SpaceSimulatorListener listener;
    private Thread thread;

    public SpaceSimulator(SpaceSimulatorListener listener, long period) {
        if (listener == null) {
            throw new IllegalArgumentException("\"listener\" can't be null");
        }
        this.spaceObjects = new HashMap<>();
        this.listener = listener;
        this.period = period;
    }

    public SpaceSimulator(SpaceSimulatorListener listener, long period, HashMap<String, SpaceObject> spaceObjects) {
        this(listener, period);
        if (spaceObjects == null) {
            throw new IllegalArgumentException("\"spaceObjects\" can't be null");
        }
        //Копируем, чтобы случайно их не изменить вовне
        spaceObjects.forEach((name, object) -> this.spaceObjects.put(name, object.copy()));
    }


    public void addSpaceObject(String name, SpaceObject spaceObject) {
        throw new NotImplementedException();
    }

    public void removeSpaceObject(String name) {
        throw new NotImplementedException();
    }

    public void getSpaceObject(String name) {
        throw new NotImplementedException();
    }

    public void pause() {
        throw new NotImplementedException();
    }

    public void resume() {
        throw new NotImplementedException();
    }
}
