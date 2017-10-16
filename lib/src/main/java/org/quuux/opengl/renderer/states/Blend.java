package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.Renderer;

public class Blend extends State {
    private final int sfactor;
    private final int dfactor;

    public Blend(int sfactor, int dfactor) {
        this.sfactor = sfactor;
        this.dfactor = dfactor;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public int getSfactor() {
        return sfactor;
    }

    public int getDfactor() {
        return dfactor;
    }
}
