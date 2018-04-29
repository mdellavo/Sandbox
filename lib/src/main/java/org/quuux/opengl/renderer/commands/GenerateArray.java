package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateArray extends Command {

    private final VAO vao;

    public GenerateArray(VAO vao) {
        this.vao = vao;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public VAO getVao() {
        return vao;
    }
}
