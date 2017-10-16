package org.quuux.opengl.renderer.states;


import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Renderer;

public class BindTexture extends State {

    private final Texture2D texture;

    public BindTexture(Texture2D texture) {
        this.texture = texture;
    }

    @Override
    void set(final Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(final Renderer renderer) {
        renderer.clear(this);
    }

    public Texture2D getTexture() {
        return texture;
    }
}
