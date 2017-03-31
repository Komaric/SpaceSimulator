package ru.komaric.spacesimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import java.util.Set;

public class Controller implements SpaceSimulatorListener {

    @FXML
    private SpaceSimulatorPane spaceSimulatorPane;

    private Set<SpaceObject> spaceObjects = null;
    private SpaceSimulator spaceSimulator = null;
    private final AnimationTimer timer;


    public Controller() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                spaceSimulatorPane.repaint(spaceObjects);
            }
        };
    }

    public void initialize(SpaceSimulator spaceSimulator) {
        if (this.spaceSimulator != null) {
            throw new IllegalStateException("\"spaceSimulator\" is already initialized");
        }
        if (spaceSimulator == null) {
            throw new IllegalArgumentException("\"spaceSimulator\" can't be null");
        }
        this.spaceSimulator = spaceSimulator;
        spaceSimulator.addListener(this);
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {

    }

    public void startAnimation() {
        checkInit();
        timer.start();
    }

    public void stopAnimation() {
        checkInit();
        timer.stop();
    }

    private void checkInit() {
        if (spaceSimulator == null) {
            throw new IllegalStateException("spaceSimulator is not initialized");
        }
    }
}
