package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.Command;

public class BindVertex implements Command {

    private final VBO vbo;
    private final VAO vao;

    public BindVertex(VBO vbo, VAO vao) {
        this.vbo = vbo;
        this.vao = vao;
    }

    @Override
    public void run(GL gl) {
        vbo.bind(gl.getGL4());
        vao.bind(gl.getGL4());
    }
}
