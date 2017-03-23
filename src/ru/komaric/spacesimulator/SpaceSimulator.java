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
        spaceObjects.forEach((name, object) -> {
            if (object == null) {
                throw new IllegalArgumentException("\"spaceObjects\" can't contain null");
            }
            this.spaceObjects.put(name, object.copy());
        });
    }


    public void addSpaceObject(String name, SpaceObject spaceObject) {
        if (name == null) {
            throw new IllegalArgumentException("\"name\" can't be null");
        }
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"spaceObject\" can't be null");
        }
        spaceObjects.put(name, spaceObject);
    }

    public void removeSpaceObject(String name) {
        spaceObjects.remove(name);
    }

    public void getSpaceObject(String name) {
        spaceObjects.get(name);
    }

    public void pause() {
        throw new NotImplementedException();
    }

    public void resume() {
        throw new NotImplementedException();
    }
}
