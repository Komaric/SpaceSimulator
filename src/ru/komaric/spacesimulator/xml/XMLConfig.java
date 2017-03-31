package ru.komaric.spacesimulator.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import ru.komaric.spacesimulator.SpaceSimulator;
import ru.komaric.spacesimulator.spaceobjects.Comet;
import ru.komaric.spacesimulator.spaceobjects.Planet;
import ru.komaric.spacesimulator.spaceobjects.Spaceship;
import ru.komaric.spacesimulator.spaceobjects.Star;

import java.io.InputStream;
import java.io.OutputStream;

public class XMLConfig {

    private static final String STAR_ALIAS = "star";
    private static final String COMET_ALIAS = "comet";
    private static final String PLANET_ALIAS = "planet";
    private static final String SPACESHIP_ALIAS = "spaceship";
    private static final String SPACE_OBJECT_SET_ALIAS = "space-objects";
    private static final String SPACE_SIMULATOR_ALIAS = "space-simulator";

    public static void save(SpaceSimulator spaceSimulator, OutputStream out) {
        if (spaceSimulator == null) {
            throw new IllegalArgumentException("\"spaceObjects\" can't be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("\"out\" can't be null");
        }
        XStream xs = createXStream();
        xs.toXML(spaceSimulator, out);
    }

    public static SpaceSimulator load(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("\"in\" can't be null");
        }
        XStream xs = createXStream();
        try {
            Object object = xs.fromXML(in);
            return (SpaceSimulator)object;
        } catch (ClassCastException
                | XMLFormatException
                | StreamException
                | ConversionException
                | CannotResolveClassException e) { //получено методом научного тыка
            throw new XMLFormatException();
        }
    }

    private static XStream createXStream() {
        XStream xs = new XStream();
        xs.alias(STAR_ALIAS, Star.class);
        xs.alias(COMET_ALIAS, Comet.class);
        xs.alias(PLANET_ALIAS , Planet.class);
        xs.alias(SPACESHIP_ALIAS, Spaceship.class);
        xs.alias(SPACE_SIMULATOR_ALIAS, SpaceSimulator.class);
        xs.registerConverter(new SpaceObjectConverter());
        xs.registerConverter(new SpaceSimulatorConverter());
        return xs;
    }
}
