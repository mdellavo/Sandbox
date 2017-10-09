package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import org.quuux.opengl.lib.Texture2D;

public class ActivateTexture extends Bind {

    int textureUnit;
    public ActivateTexture(int textureUnit, Texture2D texture) {
        super(texture);
        this.textureUnit = textureUnit;
    }

    public ActivateTexture(Texture2D texture) {
        this(GL4.GL_TEXTURE0, texture);
    }

    @Override
    public void clearState(GL gl) {
        super.clearState(gl);
    }

    @Override
    public void setState(GL gl) {
        super.setState(gl);
        gl.glActiveTexture(textureUnit);
    }
}
