package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

public class GenerateFramebuffer implements Command {

    private final FrameBuffer framebuffer;

    public GenerateFramebuffer(final FrameBuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public FrameBuffer getFramebuffer() {
        return framebuffer;
    }
}
