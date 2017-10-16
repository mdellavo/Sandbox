package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class DrawArrays implements Command {

    public enum Mode {
        Triangles,
        Points,
    }

    private final Mode mode;
    private final int first;
    private final int count;

    public DrawArrays(Mode mode, int first, int count) {
        this.mode = mode;
        this.first = first;
        this.count = count;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }


    public Mode getMode() {
        return mode;
    }

    public int getFirst() {
        return first;
    }

    public int getCount() {
        return count;
    }

}
