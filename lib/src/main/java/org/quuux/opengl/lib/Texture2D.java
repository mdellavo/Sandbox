package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.GenerateMipMap;
import org.quuux.opengl.renderer.commands.GenerateTexture2D;
import org.quuux.opengl.renderer.commands.LoadTexture;
import org.quuux.opengl.renderer.commands.TextureParameter;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.State;
import org.quuux.opengl.renderer.states.TextureTarget;
import org.quuux.opengl.util.ResourceUtil;

public class Texture2D extends Texture {
    private final ResourceUtil.Bitmap bitmap;

    LoadTexture.Format internalFormat = LoadTexture.Format.RGBA;
    LoadTexture.Format format = LoadTexture.Format.RGBA;

    public Texture2D(ResourceUtil.Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Command initialize(int unit) {
        CommandList rv = new CommandList();
        rv.add(new GenerateTexture2D(this));
        rv.add(new ActivateTexture(unit));

        BindTexture ctx = new BindTexture(TextureTarget.TEXTURE_2D,this);
        rv.add(ctx);

        ctx.add(new LoadTexture(this, TextureTarget.TEXTURE_2D, internalFormat, bitmap.width, bitmap.height, format, bitmap.buffer));
        ctx.add(new TextureParameter(TextureTarget.TEXTURE_2D, TextureParameter.Parameter.MIN_FILTER, TextureParameter.Filter.LINEAR_MIPMAP_LINEAR));
        ctx.add(new TextureParameter(TextureTarget.TEXTURE_2D, TextureParameter.Parameter.MAG_FILTER, TextureParameter.Filter.LINEAR));
        ctx.add(new TextureParameter(TextureTarget.TEXTURE_2D, TextureParameter.Parameter.WRAP_S, TextureParameter.Wrap.REPEAT));
        ctx.add(new TextureParameter(TextureTarget.TEXTURE_2D, TextureParameter.Parameter.WRAP_T, TextureParameter.Wrap.REPEAT));
        ctx.add(new GenerateMipMap(TextureTarget.TEXTURE_2D));
        return rv;
    }

    @Override
    public State bind(int unit) {
        return new BatchState(
                new ActivateTexture(unit),
                new BindTexture(TextureTarget.TEXTURE_2D, this)
        );
    }
}
