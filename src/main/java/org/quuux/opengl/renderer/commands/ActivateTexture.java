package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;

public class ActivateTexture implements Command {

    int textureId;
    Texture2D texture;

    public ActivateTexture(int textueId, Texture2D texture) {
        this.texture = texture;
        this.textureId = textueId;
    }

    public ActivateTexture(Texture2D texture) {
        this(GL4.GL_TEXTURE0, texture);
    }

    @Override
    public void run(GL gl) {
        gl.glActiveTexture(textureId);
        texture.bind(gl.getGL4());
    }
}
