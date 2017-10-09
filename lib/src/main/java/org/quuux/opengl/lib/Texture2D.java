package org.quuux.opengl.lib;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.GLBuffers;

import org.quuux.opengl.renderer.Bindable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture2D implements Bindable {

    public int texture;

    public Texture2D(GL gl) {
        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);
        gl.glGenTextures(1, buffer);
        texture = buffer.get(0);
    }

    public void bind(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
    }

    public void clear(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    public void attach(GL gl, int internalFormat, int width, int height, int format, ByteBuffer buffer) {
        bind(gl);

        if (buffer != null)
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4);

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL.GL_UNSIGNED_BYTE, buffer);
    }

    public void setFilterParameters(GL gl, int min, int max) {
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, min);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, max);
    }
}
