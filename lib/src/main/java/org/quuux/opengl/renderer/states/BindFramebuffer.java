package org.quuux.opengl.renderer.states;

import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.renderer.Renderer;

public class BindFramebuffer extends State {

    private final FrameBuffer framebuffer;

    public BindFramebuffer(FrameBuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public FrameBuffer getFramebuffer() {
        return framebuffer;
    }
}
