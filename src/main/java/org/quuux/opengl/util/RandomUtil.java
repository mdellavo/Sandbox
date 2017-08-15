package org.quuux.opengl.util;

import java.util.Random;

public class RandomUtil {

    private static Random instance;

    public static Random getInstance() {
        if (instance == null)
            instance = new Random();
        return instance;
    }

}
