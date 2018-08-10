package org.quuux.opengl.util;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class ResourceUtil {

    public static class DecodedImage {
        public int width, height;
        public ByteBuffer buffer;
        public DecodedImage(ByteBuffer buffer, int width, int height) {
            this.buffer = buffer;
            this.width = width;
            this.height = height;
        }
    }

    public static String slurp(InputStream is) {
        Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static InputStream getResource(String name) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(name);
    }

    public static String getStringResource(String name) {
        return slurp(getResource(name));
    }

    public static DecodedImage getPNGResource(String name) {
        System.out.println(String.format("load png %s", name));

        DecodedImage rv = null;
        try {
            PNGDecoder decoder = new PNGDecoder(getResource(name));

            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();

            rv = new DecodedImage(buffer, decoder.getWidth(), decoder.getHeight());
            System.out.println(String.format("load png %s (width=%s, height=%s)", name, rv.width, rv.height));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public static Obj loadObj(String name) {
        Obj rv = null;
        try {
            InputStream in = getResource(name);
            rv = ObjReader.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rv;
    }
}
