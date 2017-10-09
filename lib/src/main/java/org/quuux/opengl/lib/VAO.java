package org.quuux.opengl.lib;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.GLBuffers;

import org.quuux.opengl.renderer.Bindable;

import java.nio.IntBuffer;

public class VAO implements Bindable {

    public int vao;

    public VAO(GL gl) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);

        gl.getGL4().glGenVertexArrays(1, tmp);
        this.vao = tmp.get(0);
        bind(gl);
    }


    public void bind(GL gl) {
        gl.getGL4().glBindVertexArray(this.vao);
    }

    public void clear(GL gl) {
        gl.getGL4().glBindVertexArray(0);
    }
}
