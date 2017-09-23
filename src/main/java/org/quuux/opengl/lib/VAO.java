package org.quuux.opengl.lib;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.IntBuffer;

public class VAO {

    public int vao;

    public VAO(GL4 gl) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);

        gl.glGenVertexArrays(1, tmp);
        this.vao = tmp.get(0);
        bind(gl);
    }


    public void bind(GL4 gl) {
        gl.glBindVertexArray(this.vao);
    }

    public void clear(GL4 gl) {
        gl.glBindVertexArray(0);
    }
}
