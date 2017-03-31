package ru.komaric.spacesimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.Planet;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;
import ru.komaric.spacesimulator.spaceobjects.Star;
import ru.komaric.spacesimulator.util.Vector;

import java.util.Set;

public class Controller implements SpaceSimulatorListener {

    @FXML
    private SpaceSimulatorPane spaceSimulatorPane;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnCenter;
    @FXML
    private Slider sliderScale;
    @FXML
    private Slider sliderSpeed;
    @FXML
    private TextField textCenterX;
    @FXML
    private TextField textCenterY;

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

        sliderScale.setValue(50);
        sliderSpeed.setValue(50);

        //короткие листенеры сразу опишу тут
        btnStart.setOnAction(e -> {
            if (!spaceSimulator.isRunning()) {
                spaceSimulator.start();
            }
            startAnimation();
        });
        btnStop.setOnAction(e -> {
            if (spaceSimulator.isRunning()) {
                spaceSimulator.stop();
            }
            stopAnimation();
        });
        btnCenter.setOnAction(e -> {
            try {
                double x = Double.valueOf(textCenterX.getText());
                double y = Double.valueOf(textCenterY.getText());
                spaceSimulatorPane.setCenter(x, y);
                spaceSimulatorPane.repaint(spaceObjects);
            } catch (NumberFormatException ex) { }
        });
        sliderScale.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                spaceSimulatorPane.setScale(newValue.doubleValue());
            }
        });
        sliderSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                spaceSimulator.setPeriod(newValue.doubleValue());
            }
        });

        //тест
        spaceSimulator.addSpaceObject(new Star("Star", Vector.Zero, 10000, 70));
        spaceSimulator.setPauseBetweenIterations(10);
        spaceSimulator.addSpaceObject(new Planet("Earth", new Vector(200, 0), 100, 30, new Vector(0, 0.00005)));
        spaceSimulator.addSpaceObject(new Planet("pl", new Vector(-400, 0), 100, 20, new Vector(0, -0.000035)));
        startAnimation();
        spaceSimulator.start();
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    public void startAnimation() {
        if (spaceSimulator == null) {
            throw new IllegalStateException("spaceSimulator is not initialized");
        }
        timer.start();
    }

    public void stopAnimation() {
        if (spaceSimulator == null) {
            throw new IllegalStateException("spaceSimulator is not initialized");
        }
        timer.stop();
    }
}
