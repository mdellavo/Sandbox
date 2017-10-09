package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;

public class Blend extends Enable {
    private final int sfactor;
    private final int dfactor;

    public Blend(int sfactor, int dfactor) {
        super(GL.GL_BLEND);
        this.sfactor = sfactor;
        this.dfactor = dfactor;
    }

    @Override
    public void setState(GL gl) {
        super.setState(gl);
        gl.glBlendFunc(sfactor, dfactor);
    }

}
