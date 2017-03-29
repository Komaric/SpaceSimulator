package ru.komaric.spacesimulator.gui.test;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class SpaceSimulatorPanel extends JPanel {

    private Set<SpaceObject> spaceObjects;
    private final int width = 1750, height = 900;

    public void setSpaceObjects(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
        setSize(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        for (SpaceObject spaceObject: spaceObjects) {
            int radius = (int)spaceObject.getRadius();
            int x = width / 2 + (int)spaceObject.getRadiusVector().getX() - radius;
            int y = height / 2 + (int)spaceObject.getRadiusVector().getY() - radius;
            g.fillOval(x, y, radius * 2, radius * 2);
        }
    }
}
