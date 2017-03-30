package ru.komaric.spacesimulator.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import ru.komaric.spacesimulator.spaceobjects.*;
import ru.komaric.spacesimulator.util.Vector;

class SpaceObjectConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        SpaceObject spaceObject = (SpaceObject)o;

        writer.addAttribute("name", spaceObject.getName());

        writer.startNode("weight");
        writer.setValue(Double.toString(spaceObject.getWeight()));
        writer.endNode();

        writer.startNode("radius");
        writer.setValue(Double.toString(spaceObject.getRadius()));
        writer.endNode();

        writer.startNode("position");
        writer.setValue(spaceObject.getRadiusVector().toString());
        writer.endNode();

        if (spaceObject instanceof MovableSpaceObject) {
            MovableSpaceObject mSpaceObject = (MovableSpaceObject) spaceObject;

            writer.startNode("speed");
            writer.setValue(mSpaceObject.getSpeed().toString());
            writer.endNode();

            if (spaceObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) spaceObject;

                writer.startNode("acceleration");
                writer.setValue(spaceship.getAcceleration().toString());
                writer.endNode();
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String name = reader.getAttribute("name");
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
                case "weight":
                    weight = Double.valueOf(reader.getValue());
                    break;
                case "radius":
                    radius = Double.valueOf(reader.getValue());
                    break;
                case "position":
                    radiusVector = Vector.fromString(reader.getValue());
                    break;
                case "speed":
                    speed = Vector.fromString(reader.getValue());
                    break;
                case "acceleration":
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
            throw new XmlFormatException();
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
