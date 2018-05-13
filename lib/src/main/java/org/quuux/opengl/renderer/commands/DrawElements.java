package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class DrawElements extends Command {

    private final DrawMode mode;
    private final int count;

    public DrawElements(final DrawMode mode, final int count) {
        this.mode = mode;
        this.count = count;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public DrawMode getMode() {
        return mode;
    }

    public int getCount() {
        return count;
    }
}
