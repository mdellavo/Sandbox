package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.GenerateMipMap;
import org.quuux.opengl.renderer.commands.GenerateTexture;
import org.quuux.opengl.renderer.commands.LoadTexture;
import org.quuux.opengl.renderer.commands.TextureParameter;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.State;
import org.quuux.opengl.renderer.states.TextureTarget;
import org.quuux.opengl.util.ResourceUtil;


public class Cubemap extends Texture {

    public enum Face {
            right,
            left,
            top,
            bottom,
            front,
            back,
    }

    private final ResourceUtil.Bitmap[] bitmaps;

    private static final TextureTarget targets[] = {
            TextureTarget.CUBE_MAP_POSITIVE_X,
            TextureTarget.CUBE_MAP_NEGATIVE_X,
            TextureTarget.CUBE_MAP_POSITIVE_Y,
            TextureTarget.CUBE_MAP_NEGATIVE_Y,
            TextureTarget.CUBE_MAP_POSITIVE_Z,
            TextureTarget.CUBE_MAP_NEGATIVE_Z,
    };

    LoadTexture.Format internalFormat = LoadTexture.Format.RGBA;
    LoadTexture.Format format = LoadTexture.Format.RGBA;

    TextureParameter.Filter minFilter = TextureParameter.Filter.LINEAR;
    TextureParameter.Filter magFilter = TextureParameter.Filter.LINEAR;

    TextureParameter.Wrap wrapS = TextureParameter.Wrap.CLAMP;
    TextureParameter.Wrap wrapT = TextureParameter.Wrap.CLAMP;

    public Cubemap(ResourceUtil.Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public Command initialize(final int unit) {
        CommandList rv = new CommandList();
        rv.add(new GenerateTexture(this));
        BindTexture ctx = new BindTexture(TextureTarget.CUBE_MAP,this);

        for (int i=0; i<bitmaps.length; i++) {
            ctx.add(new LoadTexture(this, targets[i], internalFormat, bitmaps[i].width, bitmaps[i].height, format, bitmaps[i].buffer));
        }
        ctx.add(new TextureParameter(TextureTarget.CUBE_MAP, TextureParameter.Parameter.MIN_FILTER, minFilter));
        ctx.add(new TextureParameter(TextureTarget.CUBE_MAP, TextureParameter.Parameter.MAG_FILTER, magFilter));
        ctx.add(new TextureParameter(TextureTarget.CUBE_MAP, TextureParameter.Parameter.WRAP_S, wrapS));
        ctx.add(new TextureParameter(TextureTarget.CUBE_MAP, TextureParameter.Parameter.WRAP_T, wrapT));
        ctx.add(new GenerateMipMap(TextureTarget.CUBE_MAP));

        return rv;
    }

    @Override
    public State bind(final int unit) {
        return new BatchState(
                new BindTexture(TextureTarget.CUBE_MAP, this)
        );
    }

    public static String getTextureKey(String key, Face face) {
        return String.format("textures/%s-%s.png", key, face.name());
    }

    public static Cubemap load(final String key) {
        ResourceUtil.Bitmap bitmaps[] = new ResourceUtil.Bitmap[Face.values().length];
        for (Face face : Face.values()) {
            ResourceUtil.Bitmap bitmap = ResourceUtil.getPNGResource(getTextureKey(key, face));
            bitmaps[face.ordinal()] = bitmap;
        }

        return new Cubemap(bitmaps);
    }

}
