package ru.komaric.spacesimulator.gui.test;

import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.SpaceSimulatorListener;
import ru.komaric.spacesimulator.spaceobjects.Planet;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;
import ru.komaric.spacesimulator.spaceobjects.Star;
import ru.komaric.spacesimulator.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class App extends JFrame implements SpaceSimulatorListener {

    SpaceSimulator spaceSimulator;
    SpaceSimulatorPanel panel;
    Button button;

    public App() {
        super("Space Simulator");
        setSize(1800, 950);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new SpaceSimulatorPanel();
        add(panel);
        Timer timer = new Timer(5, (e) -> panel.repaint());
        timer.start();

        button = new Button("start");
        add(button);
        button.addActionListener((l) -> {
            SpaceObject star = new Star("Star", Vector.Zero, 10000, 70);
            SpaceObject planet = new Planet("Earth", new Vector(200, 0), 100, 30, new Vector(0, -0.00005));
            SpaceObject planet0 = new Planet("pl", new Vector(-400, 0), 100, 20, new Vector(0, 0.000035));
            spaceSimulator.addSpaceObject(star);
            spaceSimulator.addSpaceObject(planet);
            spaceSimulator.addSpaceObject(planet0);

//            SpaceObject star = new Star("Star", Vector.Zero, 10000, 70);
//            SpaceObject planet = new Planet("Earth", new Vector(200, 0), 100, 30, new Vector(0, 0));
//            spaceSimulator.addSpaceObject(star.getName(), star);
//            spaceSimulator.addSpaceObject(planet.getName(), planet);
        });



        spaceSimulator = new SpaceSimulator(this, 10);
        spaceSimulator.setFadeFactor(0.01);
        spaceSimulator.start();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void onIteration(Set<SpaceObject> spaceObjects) {
        panel.setSpaceObjects(spaceObjects);
    }

    @Override
    public void dispose() {
        spaceSimulator.stop();
        super.dispose();
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
