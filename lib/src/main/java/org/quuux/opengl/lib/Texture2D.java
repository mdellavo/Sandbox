package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.GenerateTexture2D;
import org.quuux.opengl.renderer.commands.LoadTexture2D;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.State;
import org.quuux.opengl.util.ResourceUtil;

public class Texture2D extends Texture {
    LoadTexture2D.Format internalFormat = LoadTexture2D.Format.RGBA;
    LoadTexture2D.Format format = LoadTexture2D.Format.RGBA;
    LoadTexture2D.Filter filter = LoadTexture2D.Filter.LINEAR;

    public Texture2D(final String key, int unit) {
        super(key, unit);
    }

    String getKey() {
        return String.format("textures/%s.png", this.key);
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateTexture2D(this));

        BatchState state = new BatchState(new BindTexture(this), new ActivateTexture(unit));
        rv.add(state);

        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource(getKey());

        state.add(new LoadTexture2D(this, internalFormat, image.width, image.height, format, image.buffer, filter, filter));

        return rv;
    }

    @Override
    public State bind() {
        return new BindTexture(this);
    }
}
