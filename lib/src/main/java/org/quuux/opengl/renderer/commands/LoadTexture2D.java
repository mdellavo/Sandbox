package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

import java.nio.ByteBuffer;

public class LoadTexture2D extends Command {

    public enum Format {
        RGB,
        RGBA,
        SRGB_ALPHA,
        RGBA16F,
    }

    public enum Filter {
        NEAREST,
        LINEAR,
        LINEAR_MIPMAP_LINEAR,
    }

    public enum Wrap {
        CLAMP,
        REPEAT,
    }

    private final Texture2D texture;
    private final Format internalFormat;
    private final int width;
    private final int height;
    private final Format format;
    private final ByteBuffer buffer;
    private final Filter min;
    private final Filter mag;
    private final Wrap wrapS;
    private final Wrap wrapT;

    public LoadTexture2D(Texture2D texture, Format internalFormat, int width, int height, Format format, ByteBuffer buffer, Filter min, Filter mag, Wrap wrapS, Wrap wrapT) {
        this.texture = texture;
        this.internalFormat = internalFormat;
        this.width = width;
        this.height = height;
        this.format = format;
        this.buffer = buffer;
        this.min = min;
        this.mag = mag;
        this.wrapS = wrapS;
        this.wrapT = wrapT;
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

    public Filter getMin() {
        return min;
    }

    public Filter getMag() {
        return mag;
    }

    public Wrap getWrapS() {
        return wrapS;
    }

    public Wrap getWrapT() {
        return wrapT;
    }
}
