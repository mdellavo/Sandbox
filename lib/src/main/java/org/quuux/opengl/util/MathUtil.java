package org.quuux.opengl.util;

public class MathUtil {

    public static double lerp(double v0, double v1, double t) {
        return (1-t)*v0 + t*v1;
    }
}
