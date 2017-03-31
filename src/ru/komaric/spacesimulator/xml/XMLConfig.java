package ru.komaric.spacesimulator.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import ru.komaric.spacesimulator.spaceobjects.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public class XMLConfig {

    private static final String STAR_ALIAS = "star";
    private static final String COMET_ALIAS = "comet";
    private static final String PLANET_ALIAS = "planet";
    private static final String SPACESHIP_ALIAS = "spaceship";
    private static final String SPACE_OBJECT_ARRAY_ALIAS = "space-objects";

    public static void save(SpaceObject[] spaceObjects, OutputStream out) {
        if (spaceObjects == null) {
            throw new IllegalArgumentException("\"spaceObjects\" can't be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("\"out\" can't be null");
        }
        for (int i = 0; i < spaceObjects.length; ++i) {
            if (spaceObjects[i] == null) {
                throw new IllegalArgumentException("\"spaceObjects\" can't contain null");
            }
        }
        XStream xs = createXStream();
        xs.toXML(spaceObjects, out);
    }

    public static void save(Collection<SpaceObject> spaceObjects, OutputStream out) {
        if (spaceObjects == null) {
            throw new IllegalArgumentException("\"spaceObjects\" can't be null");
        }
        SpaceObject[] array = new SpaceObject[spaceObjects.size()];
        array = spaceObjects.toArray(array);
        save(array, out);
    }

    public static SpaceObject[] load(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("\"in\" can't be null");
        }
        XStream xs = createXStream();
        try {
            Object object = xs.fromXML(in);
            return (SpaceObject[])object;
        } catch (ClassCastException | XmlFormatException | ConversionException e) {
            throw new XmlFormatException();
        }
    }

    private static XStream createXStream() {
        XStream xs = new XStream();
        xs.alias(STAR_ALIAS, Star.class);
        xs.alias(COMET_ALIAS, Comet.class);
        xs.alias(PLANET_ALIAS , Planet.class);
        xs.alias(SPACESHIP_ALIAS, Spaceship.class);
        xs.alias(SPACE_OBJECT_ARRAY_ALIAS, SpaceObject[].class);
        xs.registerConverter(new SpaceObjectConverter());
        return xs;
    }
}
