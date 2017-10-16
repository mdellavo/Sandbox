package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class EnableVertexAttribArray implements Command {

    private final int index;

    public EnableVertexAttribArray(int index) {
        this.index = index;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public int getIndex() {
        return index;
    }
}
