package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateBuffer extends Command {

    private final VBO vbo;

    public GenerateBuffer(VBO vbo) {
        this.vbo = vbo;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public VBO getVbo() {
        return vbo;
    }
}
