package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class CreateProgram extends Command {

    private final ShaderProgram program;

    public CreateProgram(ShaderProgram program) {
        this.program = program;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public ShaderProgram getProgram() {
        return program;
    }
}
