package org.quuux.opengl.util;

import java.io.*;
import java.util.Scanner;

public class ResourceUtil {

    public static String slurp(InputStream is) {
        Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String getStringResource(String name) {
        InputStream is = ResourceUtil.class.getClassLoader().getResourceAsStream(name);
        return slurp(is);
    }
}
