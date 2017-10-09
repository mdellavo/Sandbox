package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

public class BatchState extends State {

    private final State[] states;

    public BatchState(State... states) {
        this.states = states;
    }

    @Override
    public void clearState(GL gl) {
        for(int i=0; i<states.length; i++)
            states[i].clearState(gl);
    }

    @Override
    public void setState(GL gl) {
        for(int i=0; i<states.length; i++)
            states[i].setState(gl);
    }
}
