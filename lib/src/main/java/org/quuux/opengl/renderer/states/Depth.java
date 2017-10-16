package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.Renderer;

public class Depth extends State {

    private final int depthFunc;

    public Depth(int depthFunc) {
        this.depthFunc = depthFunc;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public int getDepthFunc() {
        return depthFunc;
    }
}
