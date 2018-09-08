package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ArrayObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateArray extends Command {

    private final ArrayObject vao;

    public GenerateArray(ArrayObject vao) {
        this.vao = vao;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public ArrayObject getVao() {
        return vao;
    }
}
