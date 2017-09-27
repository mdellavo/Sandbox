package org.quuux.opengl.renderer.states;

import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.BatchState;

public class BindVertex extends BatchState {

    public BindVertex(VBO vbo, VAO vao) {
        super(new Bind(vbo), new Bind(vao));
    }
}
