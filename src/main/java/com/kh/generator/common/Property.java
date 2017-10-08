package com.kh.generator.common;

import java.util.ResourceBundle;

public class Property {

    private static ResourceBundle properties;

    static {
        properties = ResourceBundle.getBundle("config");
    }

    public static String getProperty(String property) {
        return properties.getString(property);
    }

}
