package org.quuux.opengl.util;

import java.util.Random;

public class RandomUtil {

    private static Random instance;

    public static Random getInstance() {
        if (instance == null)
            instance = new Random();
        return instance;
    }

    public static float randomRange(float min, float max) {
        return min + ((max-min) * getInstance().nextFloat());
    }

    public static int randomInt(int min, int max) {
        return min + getInstance().nextInt(max-min);
    }
}
