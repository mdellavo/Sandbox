package org.quuux.opengl.renderer.states;


import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.renderer.Renderer;

public class BindArray extends State {
    private final VAO vao;

    public BindArray(VAO vao) {
        this.vao = vao;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public VAO getVAO() {
        return vao;
    }
}
