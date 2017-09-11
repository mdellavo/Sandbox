package org.quuux.opengl.util;

public class Log {

    public static void out(final String format, final Object... args) {
        final String message = String.format(format, args);
        System.out.println(message);
    }

}
