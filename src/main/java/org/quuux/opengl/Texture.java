package org.quuux.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    public int texture;

    public Texture(GL4 gl) {
        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);
        gl.glGenTextures(1, buffer);
        texture = buffer.get(0);
    }

    public void bind(GL4 gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
    }

    public void attach(GL4 gl, int width, int height, int format, ByteBuffer buffer) {
        bind(gl);

        gl.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 4);
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, format, width, height, 0, format, GL4.GL_UNSIGNED_BYTE, buffer);
    }
}
