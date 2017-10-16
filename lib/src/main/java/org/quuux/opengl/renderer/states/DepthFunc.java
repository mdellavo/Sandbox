package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class DepthFunc implements Command {

    public enum Function {
        LESS,
    }

    private final Function depthFunc;

    public DepthFunc(Function depthFunc) {
        this.depthFunc = depthFunc;
    }

    public Function getDepthFunc() {
        return depthFunc;
    }

    @Override
    public void run(Renderer renderer) {
        renderer.run(this);
    }
}
