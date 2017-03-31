package ru.komaric.spacesimulator.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import ru.komaric.spacesimulator.spaceobjects.*;
import ru.komaric.spacesimulator.util.Vector;

class SpaceObjectConverter implements Converter {

    private static final String NAME = "name";
    private static final String WEIGHT = "weight";
    private static final String RADIUS = "radius";
    private static final String POSITION = "position";
    private static final String SPEED = "speed";
    private static final String ACCELERATION = "acceleration";

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        SpaceObject spaceObject = (SpaceObject)o;

        writer.addAttribute(NAME, spaceObject.getName());

        writer.startNode(WEIGHT);
        writer.setValue(Double.toString(spaceObject.getWeight()));
        writer.endNode();

        writer.startNode(RADIUS);
        writer.setValue(Double.toString(spaceObject.getRadius()));
        writer.endNode();

        writer.startNode(POSITION);
        writer.setValue(spaceObject.getRadiusVector().toString());
        writer.endNode();

        if (spaceObject instanceof MovableSpaceObject) {
            MovableSpaceObject mSpaceObject = (MovableSpaceObject) spaceObject;

            writer.startNode(SPEED);
            writer.setValue(mSpaceObject.getSpeed().toString());
            writer.endNode();

            if (spaceObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) spaceObject;

                writer.startNode(ACCELERATION);
                writer.setValue(spaceship.getAcceleration().toString());
                writer.endNode();
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String name = reader.getAttribute(NAME);
        Double
                weight = null,
                radius = null; //Чтобы были nullable
        Vector
                radiusVector = null,
                speed = null,
                acceleration = null;

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            switch (reader.getNodeName()) {
                case WEIGHT:
                    weight = Double.valueOf(reader.getValue());
                    break;
                case RADIUS:
                    radius = Double.valueOf(reader.getValue());
                    break;
                case POSITION:
                    radiusVector = Vector.fromString(reader.getValue());
                    break;
                case SPEED:
                    speed = Vector.fromString(reader.getValue());
                    break;
                case ACCELERATION:
                    acceleration = Vector.fromString(reader.getValue());
                    break;
            }
            reader.moveUp();
        }

        SpaceObject spaceObject = null;
        Class type = context.getRequiredType();
        try {
            if (type == Star.class) {
                spaceObject = new Star(name, radiusVector, weight, radius);
            } else if (type == Comet.class) {
                spaceObject = new Comet(name, radiusVector, weight, radius, speed);
            } else if (type == Planet.class) {
                spaceObject = new Planet(name, radiusVector, weight, radius, speed);
            } else if (type == Spaceship.class) {
                spaceObject = new Spaceship(name, radiusVector, weight, speed, acceleration);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new XMLFormatException();
        }

        return spaceObject;
    }

    @Override
    public boolean canConvert(Class cl) {
        return cl.equals(Star.class)
                || cl.equals(Comet.class)
                || cl.equals(Planet.class)
                || cl.equals(Spaceship.class);
    }
}
