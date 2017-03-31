package ru.komaric.spacesimulator.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

import java.util.HashSet;
import java.util.Set;

class SpaceSimulatorConverter implements Converter {

    private static final String PERIOD = "period";
    private static final String FADE_FACTOR = "fade-factor";
    private static final String PAUSE_BETWEEN_ITERATIONS = "pause";
    private static final String SPACE_OBJECTS = "space-objects";

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        SpaceSimulator spaceSimulator = (SpaceSimulator) o;

        writer.startNode(PERIOD);
        writer.setValue(Double.toString(spaceSimulator.getPeriod()));
        writer.endNode();

        writer.startNode(FADE_FACTOR);
        writer.setValue(Double.toString(spaceSimulator.getFadeFactor()));
        writer.endNode();

        writer.startNode(PAUSE_BETWEEN_ITERATIONS);
        writer.setValue(Long.toString(spaceSimulator.getPauseBetweenIterations()));
        writer.endNode();

        writer.startNode(SPACE_OBJECTS);
        context.convertAnother(spaceSimulator.getSpaceObjects());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Double period = null;
        Double fadeFactor = null;
        Long pause = null;
        HashSet<SpaceObject> spaceObjects = null;

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            switch (reader.getNodeName()) {
                case PERIOD:
                    period = Double.valueOf(reader.getValue());
                    break;
                case FADE_FACTOR:
                    fadeFactor = Double.valueOf(reader.getValue());
                    break;
                case PAUSE_BETWEEN_ITERATIONS:
                    pause = Long.valueOf(reader.getValue());
                    break;
                case SPACE_OBJECTS:
                    try {
                        spaceObjects = (HashSet<SpaceObject>) context.convertAnother(null, Set.class);
                    } catch (ClassCastException e) {
                        throw new XMLFormatException();
                    }
                    break;
            }
            reader.moveUp();
        }
        if (period == null || fadeFactor == null || pause == null) {
            throw new XMLFormatException();
        }
        SpaceSimulator spaceSimulator = new SpaceSimulator(period, fadeFactor);
        spaceSimulator.setPauseBetweenIterations(pause);
        if (spaceObjects != null) {
            spaceObjects.forEach(spaceSimulator::addSpaceObject);
        }

        return spaceSimulator;
    }

    @Override
    public boolean canConvert(Class cl) {
        return cl.equals(SpaceSimulator.class);
    }
}
