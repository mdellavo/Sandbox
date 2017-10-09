package org.quuux.opengl.util;

import java.util.Random;

public class RandomUtil {

    private static Random instance;

    public static Random getInstance() {
        if (instance == null)
            instance = new Random();
        return instance;
    }

    public static double randomRange(double min, double max) {
        return min + ((max-min) * getInstance().nextDouble());
    }

    public static int randomInt(int min, int max) {
        return min + getInstance().nextInt(max-min);
    }
}
