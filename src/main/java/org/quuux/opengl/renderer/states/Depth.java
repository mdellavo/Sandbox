package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;

public class Depth extends Enable {

    private final int depthFunc;

    public Depth(int depthFunc) {
        super(GL.GL_DEPTH_TEST);
        this.depthFunc = depthFunc;
    }

    @Override
    public void setState(GL gl) {
        super.setState(gl);
        gl.glDepthFunc(depthFunc);
    }
}
