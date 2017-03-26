package ru.komaric.spacesimulator;

import ru.komaric.spacesimulator.spaceobjects.*;
import ru.komaric.spacesimulator.util.Vector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class SpaceSimulator {

    private static final double G = 6.67e-11;

    //period в секундах
    private long period;
    private HashMap<String, SpaceObject> spaceObjects;
    private SpaceSimulatorListener listener;
    private Thread thread;
    private float fadeFactor;

    public SpaceSimulator(SpaceSimulatorListener listener, long period) {
        if (listener == null) {
            throw new IllegalArgumentException("\"listener\" can't be null");
        }
        this.spaceObjects = new HashMap<>();
        this.listener = listener;
        this.period = period;
        this.fadeFactor = 1;
    }

    public SpaceSimulator(SpaceSimulatorListener listener, long period, Map<String, SpaceObject> spaceObjects) {
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
        spaceObjects.put(name, spaceObject.copy());
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        if (period <= 0) {
            throw new IllegalArgumentException("\"period\" must be positive");
        }
        this.period = period;
    }

    public float getFadeFactor() {
        return fadeFactor;
    }

    public void setFadeFactor(float fadeFactor) {
        if (fadeFactor <= 0) {
            throw new IllegalArgumentException("\"fadeFactor\" must be positive");
        }
        this.fadeFactor = fadeFactor;
    }

    public void removeSpaceObject(String name) {
        spaceObjects.remove(name);
    }

    public void getSpaceObject(String name) {
        spaceObjects.get(name).copy();
    }

    public void pause() {
        throw new NotImplementedException();
    }

    public void resume() {
        throw new NotImplementedException();
    }

    private class IterationTask implements Runnable {
        @Override
        public void run() {
            //пока так
            while (true) {
                //Сразу делаем копию, чтобы не блокировать основой поток на протяжении всего пересчёта
                HashMap<String, SpaceObject> updated = new HashMap<>();
                HashMap<String, SpaceObject> old = new HashMap<>();
                long period;
                double fadeFactor;
                synchronized (SpaceSimulator.this) {
                    spaceObjects.forEach((name, object) -> old.put(name, object.copy()));
                    period = SpaceSimulator.this.period;
                    fadeFactor = SpaceSimulator.this.fadeFactor;
                }
                old.forEach((name, oldSpaceObject) -> {
                    SpaceObject spaceObject = oldSpaceObject.copy();
                    if (spaceObject instanceof MovableSpaceObject) {
                        MovableSpaceObject mSpaceObject = (MovableSpaceObject) spaceObject;
                        Vector totalForce = Vector.Zero;
                        for (SpaceObject so : old.values()) {
                            if (so == mSpaceObject) {
                                continue;
                            }
                            Vector force = mSpaceObject.getRadiusVector().sub(so.getRadiusVector()).normalize();
                            double distance = so.distance(mSpaceObject);
                            force = force.multiply(G * mSpaceObject.getWeight() * so.getWeight() / distance / distance);
                            totalForce = totalForce.add(force);
                        }
                        Vector acceleration = totalForce.divide(mSpaceObject.getWeight());
                        if (mSpaceObject instanceof Spaceship) {
                            acceleration = acceleration.add(((Spaceship) mSpaceObject).getAcceleration());
                        }
                        Vector speed = mSpaceObject.getSpeed().add(acceleration.multiply(period));
                        mSpaceObject.setSpeed(speed);
                        Vector radiusVector = mSpaceObject.getRadiusVector()
                                .add(speed.multiply(period))
                                .add(acceleration.multiply(period * period / 2));
                        mSpaceObject.setRadiusVector(radiusVector);
                    }
                    if (spaceObject instanceof Fadable) {
                        float fadeSpeed = 0;
                        for (SpaceObject so: old.values()){
                            if (so instanceof Star) {
                                double distance = oldSpaceObject.distance(so);
                                fadeSpeed += fadeFactor / distance / distance;
                            }
                        }
                        double radius = Math.max(0, oldSpaceObject.getRadius() - fadeSpeed * period);
                        spaceObject.setRadius(radius);
                    }
                    if (spaceObject instanceof Destroyable && ((Destroyable) spaceObject).isDestroyed()) {
                        return;
                    }
                    updated.put(name, spaceObject);
                });
                HashMap<String, SpaceObject> copy = new HashMap<>();
                updated.forEach((name, object) -> copy.put(name, object.copy()));
                synchronized (SpaceSimulator.this) {
                    spaceObjects = updated;
                }
                listener.onIteration(copy);
            }
        }
    }
}
