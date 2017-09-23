package org.quuux.opengl.lib;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.IntBuffer;

public class VBO {
    public final int vbo;

    public VBO(GL4 gl) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, tmp);
        this.vbo = tmp.get(0);
        bind(gl);
    }

    public void bind(GL4 gl) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo);
    }

    public void clear(GL4 gl) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }
}
