package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateFramebuffer extends Command {

    private final FrameBuffer framebuffer;
    private final Texture2D texture;

    public GenerateFramebuffer(final FrameBuffer framebuffer, final Texture2D texture) {
        this.framebuffer = framebuffer;
        this.texture = texture;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public FrameBuffer getFramebuffer() {
        return framebuffer;
    }

    public Texture2D getTexture() {
        return texture;
    }
}
