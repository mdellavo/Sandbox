package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateBuffer extends Command {

    private final BufferObject vbo;

    public GenerateBuffer(BufferObject vbo) {
        this.vbo = vbo;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public BufferObject getVbo() {
        return vbo;
    }
}
