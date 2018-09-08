package org.quuux.opengl.renderer.states;


import org.quuux.opengl.lib.ArrayObject;
import org.quuux.opengl.renderer.Renderer;

public class BindArray extends State {
    private final ArrayObject vao;

    public BindArray(ArrayObject vao) {
        this.vao = vao;
    }

    @Override
    public void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    public void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public ArrayObject getVAO() {
        return vao;
    }
}
