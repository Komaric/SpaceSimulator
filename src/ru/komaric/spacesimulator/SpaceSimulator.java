package ru.komaric.spacesimulator;

import ru.komaric.spacesimulator.spaceobjects.*;
import ru.komaric.spacesimulator.util.QueueItem;
import ru.komaric.spacesimulator.util.Vector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpaceSimulator {

    private static final double G = 6.67e-11;

    //period в секундах
    private double period;
    private double fadeFactor;
    private Thread thread;
    private HashMap<String, SpaceObject> spaceObjects;
    private final ArrayList<SpaceSimulatorListener> listeners = new ArrayList<>();

    //На скорость работы очень сильно влияет количество объектов,
    //поэтому при необходимости будем добавлять оверхед, что бы это небыло особо заметно
    private long pauseBetweenIterations = 0;

    private final LinkedList<QueueItem> queue = new LinkedList<>();

    public SpaceSimulator(double period, double fadeFactor) {
        this.period = period;
        this.fadeFactor = fadeFactor;
        this.spaceObjects = new HashMap<>();
    }

    public void addListener(SpaceSimulatorListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("\"listener\" can't be null");
        }
        listeners.add(listener);
    }

    public void removeListener(SpaceSimulatorListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("\"listener\" can't be null");
        }
        listeners.remove(listener);
    }

    //если поток пересчёта запущен, то сохраненяем добавленные или удалённые объекты в очередь
    public void addSpaceObject(SpaceObject spaceObject) {
        if (!isRunning()) {
            synchronized (this) {
                spaceObjects.put(spaceObject.getName(), spaceObject.copy());
            }
        } else {
            queue.addLast(QueueItem.Add(spaceObject.copy()));
        }
    }

    public void removeSpaceObject(String name) {
        if (name == null) {
            throw new IllegalArgumentException("\"name\" can't be null");
        }
        if (!isRunning()) {
            synchronized (this) {
                spaceObjects.remove(name);
            }
        } else {
            queue.addLast(QueueItem.Remove(name));
        }
    }

    public Set<SpaceObject> getSpaceObjects() {
        boolean running = isRunning();
        if (running) {
            stop();
        }
        Set<SpaceObject> spaceObjects = this.spaceObjects.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(SpaceObject::copy)
                .collect(Collectors.toSet());
        if (running) {
            start();
        }
        return spaceObjects;
    }

    public void removeSpaceObject(SpaceObject spaceObject) {
        if (spaceObject == null) {
            throw new IllegalArgumentException("\"spaceObject\" can't be null");
        }
        removeSpaceObject(spaceObject.getName());
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        if (period <= 0) {
            throw new IllegalArgumentException("\"period\" must be positive");
        }
        this.period = period;
    }

    public double getFadeFactor() {
        return fadeFactor;
    }

    public void setFadeFactor(double fadeFactor) {
        if (fadeFactor <= 0) {
            throw new IllegalArgumentException("\"fadeFactor\" must be positive");
        }
        this.fadeFactor = fadeFactor;
    }

    public void start() {
        synchronized (this) {
            if (isRunning()) {
                throw new IllegalThreadStateException("Thread already started");
            }
            thread = new Thread(iterationTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void stop() {
        synchronized (this) {
            if (!isRunning()) {
                throw new IllegalThreadStateException("Thread already stopped");
            }
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            thread = null;
            executeQueue(spaceObjects);
        }
    }

    public boolean isRunning() {
        return thread != null;
    }

    private void executeQueue(HashMap<String, SpaceObject> spaceObjects) {
        while (!queue.isEmpty()) {
            QueueItem operation = queue.getFirst();
            switch (operation.getType()) {
                case Add:
                    spaceObjects.put(operation.getName(), operation.getSpaceObject());
                    break;
                case Remove:
                    spaceObjects.remove(operation.getName());
                    break;
            }
            queue.removeFirst();
        }
    }

    public void setPauseBetweenIterations(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("\"millis\" must be non-negative");
        }
        this.pauseBetweenIterations = millis;
    }

    public long getPauseBetweenIterations() {
        return pauseBetweenIterations;
    }

    private Runnable iterationTask = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                double period = SpaceSimulator.this.period;
                double fadeFactor = SpaceSimulator.this.fadeFactor;
                HashMap<String, SpaceObject> oldMap = spaceObjects;
                HashMap<String, SpaceObject> updatedMap = new HashMap<>(spaceObjects.size());
                synchronized (queue) {
                    executeQueue(oldMap);
                }
                for (HashMap.Entry<String, SpaceObject> entry : oldMap.entrySet()) {
                    //чтобы не ждать полного пересчёта, в случае прерывания потока
                    if (Thread.interrupted()) {
                        return;
                    }
                    String name = entry.getKey();
                    SpaceObject oldSpaceObject = entry.getValue();
                    SpaceObject spaceObject = oldSpaceObject.copy();
                    if (spaceObject instanceof MovableSpaceObject) {
                        MovableSpaceObject mSpaceObject = (MovableSpaceObject) spaceObject;
                        Vector totalForce = Vector.Zero;
                        for (SpaceObject so : oldMap.values()) {
                            if (so == oldSpaceObject) {
                                continue;
                            }
                            Vector force = so.getRadiusVector().sub(mSpaceObject.getRadiusVector()).normalize();
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
                        double fadeSpeed = 0;
                        for (SpaceObject so : oldMap.values()) {
                            if (so instanceof Star) {
                                double distance = oldSpaceObject.distance(so);
                                fadeSpeed += fadeFactor / distance / distance;
                            }
                        }
                        double oldRadius = oldSpaceObject.getRadius();
                        double radius = Math.max(0, oldRadius - fadeSpeed * period);
                        spaceObject.setRadius(radius);
                        double weight = (radius / oldRadius) * (radius / oldRadius) * oldSpaceObject.getWeight();
                        spaceObject.setWeight(weight);
                    }
                    if (spaceObject instanceof Destroyable && ((Destroyable) spaceObject).isDestroyed()) {
                        continue;
                    }
                    updatedMap.put(name, spaceObject);
                }

                //страшная обработка столкновений объектов
                int length = updatedMap.size();
                SpaceObject[] arr = updatedMap.values().toArray(new SpaceObject[length]);
                boolean done = false;
                while (!done) {
                    done = true;
                    for (int i = 0; i < length; ++i) {
                        for (int j = 0; j < length; ++j) {
                            if (i == j) {
                                continue;
                            }
                            if (arr[i].isOverlapped(arr[j])) {
                                done = false;
                                SpaceObject updated, removed;
                                int removedPosition;
                                if (arr[i].getWeight() >= arr[j].getWeight()) {
                                    updated = arr[i];
                                    removed = arr[j];
                                    removedPosition = j;
                                } else {
                                    updated = arr[j];
                                    removed = arr[i];
                                    removedPosition = i;
                                }
                                arr[removedPosition] = arr[length - 1];
                                arr[length - 1] = removed;
                                --length;
                                --j;
                                double updatedWeight = updated.getWeight();
                                double removedWeight = removed.getWeight();
                                if (updated instanceof MovableSpaceObject) {
                                    MovableSpaceObject mUpdated = (MovableSpaceObject) updated;
                                    Vector removedSpeed;
                                    try {
                                        removedSpeed = ((MovableSpaceObject) removed).getSpeed();
                                    } catch (ClassCastException e) {
                                        removedSpeed = Vector.Zero;
                                    }
                                    Vector updatedImpulse = mUpdated.getSpeed().multiply(updatedWeight);
                                    Vector removedImpulse = removedSpeed.multiply(removedWeight);
                                    Vector totalSpeed = updatedImpulse
                                            .add(removedImpulse)
                                            .divide(updatedWeight + removedWeight);
                                    mUpdated.setSpeed(totalSpeed);
                                }
                                updated.setWeight(updatedWeight + removedWeight);
                                updated.setRadius(updated.getRadius() + removed.getRadius());
                            }
                        }
                    }
                }
                for (int i = length; i < arr.length; ++i) {
                    updatedMap.remove(arr[i].getName());
                }

                synchronized (SpaceSimulator.this) {
                    spaceObjects = updatedMap;
                }
                //Для каждого листенера создаём свою копию;
                Stream<SpaceObject> stream = updatedMap.entrySet()
                        .stream()
                        .map(Map.Entry::getValue);
                //запускаем листенеры в новом потоке для того, что бы в них можно было вызвать stop()
                Thread listenersThread = new Thread(() ->
                        listeners.forEach((listener -> listener.onIteration(stream
                                .map(SpaceObject::copy)
                                .collect(Collectors.toSet()))))
                );
                listenersThread.start();
                try {
                    listenersThread.join();
                    Thread.sleep(pauseBetweenIterations);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    };
}
