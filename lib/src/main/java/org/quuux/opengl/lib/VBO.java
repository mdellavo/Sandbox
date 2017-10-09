package org.quuux.opengl.lib;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.GLBuffers;

import org.quuux.opengl.renderer.Bindable;

import java.nio.IntBuffer;

public class VBO implements Bindable {
    public final int vbo;

    public VBO(GL gl) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, tmp);
        this.vbo = tmp.get(0);
        bind(gl);
    }

    public void bind(GL gl) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, this.vbo);
    }

    public void clear(GL gl) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }
}
