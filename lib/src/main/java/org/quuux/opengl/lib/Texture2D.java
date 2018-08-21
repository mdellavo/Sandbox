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
    private final ResourceUtil.Bitmap bitmap;

    LoadTexture2D.Format internalFormat = LoadTexture2D.Format.RGBA;
    LoadTexture2D.Format format = LoadTexture2D.Format.RGBA;
    LoadTexture2D.Filter filter = LoadTexture2D.Filter.LINEAR;

    public Texture2D(ResourceUtil.Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Command initialize(int unit) {
        CommandList rv = new CommandList();
        rv.add(new GenerateTexture2D(this));
        rv.add(new ActivateTexture(unit));

        BindTexture ctx = new BindTexture(this);
        rv.add(ctx);

        ctx.add(new LoadTexture2D(this, internalFormat, bitmap.width, bitmap.height, format, bitmap.buffer, filter, filter));

        return rv;
    }

    @Override
    public State bind(int unit) {
        return new BatchState(
                new ActivateTexture(unit),
                new BindTexture(this)
        );
    }
}
