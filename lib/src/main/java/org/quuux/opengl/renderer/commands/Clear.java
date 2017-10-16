package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

import java.util.EnumSet;

public class Clear implements Command {

    public enum Mode {
        COLOR_BUFFER,
        DEPTH_BUFFER,
    }

    private final EnumSet<Mode> mask = EnumSet.noneOf(Mode.class);

    public Clear(Mode... modes) {
        for (int i=0; i<modes.length; i++)
            mask.add(modes[i]);
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public EnumSet<Mode> getMask() {
        return mask;
    }
}
