package com.nullpoint.recorder.util;

import java.util.HashMap;

public class PropertiesContainer {
    private static PropertiesContainer sProperties = null;
    private HashMap<String, String> mProperties;

    private PropertiesContainer() {
        mProperties = new HashMap<String, String>();
    }

    public static PropertiesContainer instance() {
        return (sProperties == null)?(sProperties = new PropertiesContainer()):sProperties;
    }

    public String getProperty(String propertyName) {
        return mProperties.get(propertyName);
    }

    public boolean load(String path) {
        return false;
    }
}