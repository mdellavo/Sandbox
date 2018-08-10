package org.quuux.opengl.renderer.states;


import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.renderer.Renderer;

public class BindTexture extends State {

    private final Texture texture;

    public BindTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    public void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public Texture getTexture() {
        return texture;
    }
}
