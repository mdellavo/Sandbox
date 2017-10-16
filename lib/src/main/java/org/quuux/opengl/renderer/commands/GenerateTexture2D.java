package org.quuux.opengl.renderer.commands;


import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateTexture2D implements Command {

    private final Texture2D texture;

    public GenerateTexture2D(Texture2D texture) {
        this.texture = texture;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public Texture2D getTexture() {
        return texture;
    }
}
