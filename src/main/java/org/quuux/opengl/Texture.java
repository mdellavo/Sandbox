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
        gl.glEnable(GL4.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
    }

    public void load(GL4 gl, int minFilter, int magFilter, int width, int height, ByteBuffer buffer) {
        bind(gl);

        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, magFilter);

        gl.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 4);
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, buffer);
        gl.glGenerateMipmap(texture);
    }
}
