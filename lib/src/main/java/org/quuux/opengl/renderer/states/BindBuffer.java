package org.quuux.opengl.renderer.states;

import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.Renderer;

public class BindBuffer extends State {
    private final VBO vbo;

    public BindBuffer(VBO vbo) {
        this.vbo = vbo;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public VBO getVBO() {
        return vbo;
    }
}
