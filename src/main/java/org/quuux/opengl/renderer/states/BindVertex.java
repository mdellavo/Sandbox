package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.State;

public class BindVertex extends State {

    private final VBO vbo;
    private final VAO vao;

    public BindVertex(VBO vbo, VAO vao) {
        this.vbo = vbo;
        this.vao = vao;
    }

    @Override
    public void clearState(GL gl) {
        vbo.clear(gl.getGL4());
        vao.clear(gl.getGL4());
    }

    @Override
    public void setState(GL gl) {
        vbo.bind(gl.getGL4());
        vao.bind(gl.getGL4());
    }
}
