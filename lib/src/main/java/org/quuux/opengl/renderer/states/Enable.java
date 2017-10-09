package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;
import org.quuux.opengl.renderer.State;

public class Enable extends State {

    private final int cap;

    public Enable(int cap) {
        this.cap = cap;
    }

    @Override
    public void clearState(GL gl) {
        gl.glDisable(cap);
    }

    @Override
    public void setState(GL gl) {
        gl.glEnable(cap);
    }
}
