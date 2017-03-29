package ru.komaric.spacesimulator.util;

import ru.komaric.spacesimulator.spaceobjects.SpaceObject;

public class QueueItem {

    public enum QueueItemType {
        Add, Remove
    }

    private final String name;
    private final SpaceObject spaceObject;
    private final QueueItemType type;

    private QueueItem(String name, SpaceObject spaceObject, QueueItemType type) {
        this.name = name;
        this.spaceObject = spaceObject;
        this.type = type;
    }

    public static QueueItem Add(SpaceObject spaceObject) {
        return new QueueItem(spaceObject.getName(), spaceObject, QueueItemType.Add);
    }

    public static QueueItem Remove(String name) {
        return new QueueItem(name, null, QueueItemType.Remove);
    }

    public String getName() {
        return name;
    }

    public SpaceObject getSpaceObject() {
        return spaceObject;
    }

    public QueueItemType getType() {
        return type;
    }
}
