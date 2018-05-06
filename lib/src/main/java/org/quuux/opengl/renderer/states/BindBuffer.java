package org.quuux.opengl.renderer.states;

import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Renderer;

public class BindBuffer extends State {
    private final BufferObject vbo;
    private final BufferType target;

    public BindBuffer(BufferType target, BufferObject vbo) {
        this.target = target;
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

    public BufferObject getVBO() {
        return vbo;
    }

    public BufferType getTarget() {
        return target;
    }
}
