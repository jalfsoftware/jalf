package com.jalfsoftware.jalf.helper;

/**
 * Halterklasse für zu ladene Maps
 */
public class Map {
    private String  name;
    private String  path;
    private boolean isDefault;

    public Map(String name, String path, boolean isDefault) {
        this.name = name;
        this.path = path;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return name;
    }
}
