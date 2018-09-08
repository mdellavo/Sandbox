package org.quuux.opengl.renderer.commands;


import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateTexture extends Command {

    private final Texture texture;

    public GenerateTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public Texture getTexture() {
        return texture;
    }
}
