package org.quuux.opengl.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLUtil {
    public static ByteBuffer byteBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf;
    }

    public static IntBuffer intBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size * 4);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf.asIntBuffer();
    }

    public static FloatBuffer floatBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size * 4);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf.asFloatBuffer();
    }

    public static FloatBuffer floatBuffer(final float[] vertices) {
        FloatBuffer buffer = floatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.position(0);
        return buffer;
    }
}
