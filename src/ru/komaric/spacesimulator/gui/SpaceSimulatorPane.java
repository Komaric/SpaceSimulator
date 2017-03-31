package ru.komaric.spacesimulator.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SpaceSimulatorPane extends Pane implements SpaceSimulatorListener {

    private Set<SpaceObject> spaceObjects = new HashSet<>();
    private double scale = 1;
    private final Canvas canvas;
    private Point2D center = new Point2D(0, 0);

    public SpaceSimulatorPane() {
        setStyle("-fx-background-color: black;");
        setMinSize(100, 100);
        canvas = new Canvas();
        widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        getChildren().add(canvas);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
    }

    public void repaint(Collection<SpaceObject> spaceObjects) {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        double width = canvas.getWidth(), height = canvas.getHeight();
        graphics.clearRect(0, 0, width, height);
        if (spaceObjects == null) {
            return;
        }
        for (SpaceObject spaceObject: spaceObjects) {
            double radius = spaceObject.getRadius() * scale;
            double x = spaceObject.getRadiusVector().getX() * scale - radius + width / 2 - center.getX() * scale;
            double y = -spaceObject.getRadiusVector().getY() * scale - radius + height / 2 - center.getY() * scale;
            graphics.fillOval(x, y, radius * 2, radius * 2);
            graphics.fillText(spaceObject.getName(), x + radius  * 2 + 5, y + radius);
        }
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(double x, double y) {
        center = new Point2D(x, y);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        if (scale > 0) {
            this.scale = scale;
        }
    }
}
