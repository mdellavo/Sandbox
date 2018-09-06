package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.states.TextureTarget;

import java.nio.ByteBuffer;

public class LoadTexture extends Command {

    public enum Format {
        RGB,
        RGBA,
        SRGB_ALPHA,
        RGBA16F,
    }

    private final Texture2D texture;
    private final TextureTarget target;
    private final Format internalFormat;
    private final int width;
    private final int height;
    private final Format format;
    private final ByteBuffer buffer;

    public LoadTexture(Texture2D texture, TextureTarget target, Format internalFormat, int width, int height, Format format, ByteBuffer buffer) {
        this.texture = texture;
        this.target = target;
        this.internalFormat = internalFormat;
        this.width = width;
        this.height = height;
        this.format = format;
        this.buffer = buffer;
    }
    
    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public Texture2D getTexture() {
        return texture;
    }

    public Format getInternalFormat() {
        return internalFormat;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Format getFormat() {
        return format;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public TextureTarget getTarget() {
        return target;
    }
}
