package org.quuux.opengl.renderer.states;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Renderer;

public class UseProgram extends State {

    private final ShaderProgram program;

    public UseProgram(ShaderProgram program) {
        this.program = program;
    }

    @Override
    public void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    public void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public ShaderProgram getProgram() {
        return program;
    }
}
