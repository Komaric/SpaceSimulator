package ru.komaric.spacesimulator.gui.test;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class SpaceSimulatorPanel extends JPanel {

    private Set<SpaceObject> spaceObjects;
    private final int width = 1750, height = 900;
    //private final int border = 300;

    public void setSpaceObjects(Set<SpaceObject> spaceObjects) {
        this.spaceObjects = spaceObjects;
        setSize(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        Set<SpaceObject> spaceObjects = this.spaceObjects;
//        double xmax = 0, xmin = 0, ymax = 0, ymin = 0;
//        for (SpaceObject sp: spaceObjects) {
//            Vector vector = sp.getRadiusVector();
//            double radius = sp.getRadius();
//            double x = vector.getX(), y = vector.getY();
//            xmax = xmax > x + radius ? xmax : x + radius;
//            xmin = xmin < x - radius ? xmin : x - radius;
//            ymax = ymax > y + radius ? ymax : y + radius;
//            ymin = ymin < y - radius ? ymin : y - radius;
//        }
//        double tmp = ymax;
//        ymax = -ymin;
//        ymin = -tmp;
//        double sWidth = xmax - xmin;
//        double sHeight = ymax - ymin;
//        double aWidth = width - 2 * border;
//        double aHeight = height - 2 * border;
//
//        double kw = aWidth / sWidth;
//        double kh = aHeight / sHeight;
//
//        double dx = 0, dy = 0;
//        double scale;
//        if (kw > kh) {
//            dx = (aWidth - kh * sWidth) / 2;
//            scale = kh;
//        } else {
//            dy = (aHeight - kw * sHeight) / 2;
//            scale = kw;
//        }

        for (SpaceObject spaceObject: spaceObjects) {
            int radius = (int)spaceObject.getRadius();
            int x = width / 2 + (int)spaceObject.getRadiusVector().getX() - radius;
            int y = height / 2 + -(int)spaceObject.getRadiusVector().getY() - radius;
            g.fillOval(x, y, radius * 2, radius * 2);

//            double radius = spaceObject.getRadius() * scale;
//            double x = dx + spaceObject.getRadiusVector().getX() * scale - radius - xmin * scale + border;
//            double y = dy - spaceObject.getRadiusVector().getY() * scale - radius - ymin * scale + border;
//            g.fillOval((int)x, (int)y, (int)(radius * 2), (int)(radius * 2));
        }
    }
}
