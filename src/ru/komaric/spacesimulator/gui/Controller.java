package ru.komaric.spacesimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.Planet;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;
import ru.komaric.spacesimulator.spaceobjects.Star;
import ru.komaric.spacesimulator.util.Vector;

import java.util.Set;

class Controller implements SpaceSimulatorListener {

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
    private final AnimationTimer animationTimer;
    private Stage stage;


    public Controller() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                spaceSimulatorPane.repaint(spaceObjects);
            }
        };
    }

    public void initialize(SpaceSimulator spaceSimulator, Stage stage) {
        if (this.spaceSimulator != null) {
            throw new IllegalStateException("\"spaceSimulator\" is already initialized");
        }
        if (spaceSimulator == null) {
            throw new IllegalArgumentException("\"spaceSimulator\" can't be null");
        }
        if (stage == null) {
            throw new IllegalArgumentException("\"stage\" can't be null");
        }
        this.spaceSimulator = spaceSimulator;
        this.stage = stage;
        spaceSimulator.addListener(this);

        btnStart.setOnAction(e -> {
            if (!spaceSimulator.isRunning()) {
                spaceSimulator.start();
            }
            animationTimer.start();
        });
        btnStop.setOnAction(e -> {
            if (spaceSimulator.isRunning()) {
                spaceSimulator.stop();
            }
            animationTimer.stop();
        });
        btnCenter.setOnAction(e -> {
            try {
                double x = Double.valueOf(textCenterX.getText());
                double y = -Double.valueOf(textCenterY.getText());
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
        btnSave.setOnAction(e -> {

        });
        btnLoad.setOnAction(e -> {

        });

        //тест
        spaceSimulator.setPauseBetweenIterations(10);
        spaceSimulator.addSpaceObject(new Star("Star", Vector.Zero, 10000, 70));
        spaceSimulator.addSpaceObject(new Planet("Earth", new Vector(200, 0), 100, 30, new Vector(0, 0.00005)));
        spaceSimulator.addSpaceObject(new Planet("pl", new Vector(-400, 0), 100, 20, new Vector(0, -0.000035)));
        animationTimer.start();
        spaceSimulator.start();
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }
}
