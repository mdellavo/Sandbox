package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.states.TextureTarget;

public class GenerateMipMap extends Command {

    private final TextureTarget target;

    public GenerateMipMap(final TextureTarget target) {
        this.target = target;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public TextureTarget getTarget() {
        return target;
    }
}
