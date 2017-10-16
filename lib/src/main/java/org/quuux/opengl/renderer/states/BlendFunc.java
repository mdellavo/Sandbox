package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class BlendFunc implements Command {

    public enum Factor {
        SRC_ALPHA,
        ONE_MINUS_SRC_ALPHA,
    }

    private final Factor sfactor;
    private final Factor dfactor;

    public BlendFunc(Factor sfactor, Factor dfactor) {
        this.sfactor = sfactor;
        this.dfactor = dfactor;
    }

    public Factor getSfactor() {
        return sfactor;
    }

    public Factor getDfactor() {
        return dfactor;
    }

    @Override
    public void run(Renderer renderer) {
        renderer.run(this);
    }
}
