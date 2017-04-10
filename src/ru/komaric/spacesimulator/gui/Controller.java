package ru.komaric.spacesimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;
import ru.komaric.spacesimulator.xml.XMLConfig;
import ru.komaric.spacesimulator.xml.XMLFormatException;

import java.io.*;
import java.util.Set;

public class Controller implements SpaceSimulatorListener {

    private final long SLIDER_SPEED_MAX_VALUE = 100;
    private double scale = 1; //начальное значение

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
    private Slider sliderSpeed;
    @FXML
    private TextField textCenterX;
    @FXML
    private TextField textCenterY;
    @FXML
    private Button btnPeriod;
    @FXML
    private TextField textPeriod;
    @FXML
    private Button btnFadeFactor;
    @FXML
    private TextField textFadeFactor;
    @FXML
    private VBox vBoxOptions;
    @FXML
    private Button btnScale;
    @FXML
    private TextField textScale;
    @FXML
    private Slider sliderScale;

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
        animationTimer.start();
    }

    public void initialize(Stage stage) {
        if (this.spaceSimulator != null) {
            throw new IllegalStateException("\"spaceSimulator\" is already initialized");
        }
        if (stage == null) {
            throw new IllegalArgumentException("\"stage\" can't be null");
        }
        this.stage = stage;
        sliderSpeed.setMax(SLIDER_SPEED_MAX_VALUE);
        textScale.setText(Double.toString(scale));
        sliderScale.setValue(0);
        spaceSimulatorPane.setScale(scale);
        sliderScale.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 0) {
                    return "* " + Double.toString(Math.pow(10, sliderScale.getMin()));
                } else {
                    return "* " + Double.toString(Math.pow(10, sliderScale.getMax()));
                }
            }
            @Override
            public Double fromString(String s) {
                return null;
            }
        });

        btnStart.setOnAction(e -> {
            if (!spaceSimulator.isRunning()) {
                spaceSimulator.start();
            }
        });
        btnStop.setOnAction(e -> {
            if (spaceSimulator.isRunning()) {
                spaceSimulator.stop();
            }
        });
        btnCenter.setOnAction(e -> {
            try {
                double x = Double.valueOf(textCenterX.getText());
                double y = -Double.valueOf(textCenterY.getText());
                spaceSimulatorPane.setCenter(x, y);
                spaceSimulatorPane.repaint(spaceObjects);
            } catch (NumberFormatException ex) {
            }
        });
        btnPeriod.setOnAction(e -> {
            try {
                double period = Double.valueOf(textPeriod.getText());
                spaceSimulator.setPeriod(period);
            } catch (NumberFormatException ex) {
            }
        });
        btnFadeFactor.setOnAction(e -> {
            try {
                double fadeFactor = Double.valueOf(textFadeFactor.getText());
                spaceSimulator.setPeriod(fadeFactor);
            } catch (NumberFormatException ex) {
            }
        });
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) ->
                spaceSimulator.setPauseBetweenIterations(SLIDER_SPEED_MAX_VALUE - newValue.longValue())
        );
        btnScale.setOnAction(e -> {
            try {
                scale = Double.valueOf(textScale.getText());
                sliderScale.setValue(0);
                spaceSimulatorPane.setScale(scale);
            } catch (NumberFormatException ex) {
            }
        });
        sliderScale.valueProperty().addListener((observable, oldValue, newValue) ->
                spaceSimulatorPane.setScale(scale * Math.pow(10, newValue.doubleValue()))
        );
        btnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save configuration");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            fileChooser.setInitialFileName("config.xml");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    XMLConfig.save(this.spaceSimulator, out);
                    out.close();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error");
                    alert.showAndWait();
                }
            }
        });
        btnLoad.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open configuration");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ALL", "*.*"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    FileInputStream in = new FileInputStream(file);
                    SpaceSimulator spaceSimulator = XMLConfig.load(in);
                    in.close();
                    setSpaceSimulator(spaceSimulator);
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error");
                    alert.showAndWait();
                } catch (XMLFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Bad file");
                    alert.showAndWait();
                }
            }
        });
    }

    public SpaceSimulator getSpaceSimulator() {
        return spaceSimulator;
    }

    public void setSpaceSimulator(SpaceSimulator spaceSimulator) {
        if (this.spaceSimulator != null) {
            if (this.spaceSimulator.isRunning()) {
                this.spaceSimulator.stop();
            }
            this.spaceSimulator.removeListener(this);
        }
        this.spaceSimulator = spaceSimulator;
        if (spaceSimulator != null) {
            spaceObjects = spaceSimulator.getSpaceObjects();
            spaceSimulator.addListener(this);
            btnSave.setDisable(false);
            vBoxOptions.setDisable(false);
            sliderSpeed.setValue(SLIDER_SPEED_MAX_VALUE - spaceSimulator.getPauseBetweenIterations());
            textPeriod.setText(Double.toString(spaceSimulator.getPeriod()));
            textFadeFactor.setText(Double.toString(spaceSimulator.getFadeFactor()));
        } else {
            spaceObjects = null;
            btnSave.setDisable(true);
            vBoxOptions.setDisable(true);
        }
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }
}
