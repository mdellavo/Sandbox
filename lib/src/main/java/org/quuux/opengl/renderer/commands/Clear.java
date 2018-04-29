package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class Clear extends Command {

    private final Mode[] modes;

    public enum Mode {
        COLOR_BUFFER,
        DEPTH_BUFFER,
    }

    public Clear(Mode... modes) {
        this.modes = modes;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public Mode[] getModes() {
        return modes;
    }
}
