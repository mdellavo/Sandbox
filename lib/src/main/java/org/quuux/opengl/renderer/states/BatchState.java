package org.quuux.opengl.renderer.states;


import org.quuux.opengl.renderer.Renderer;

import java.util.Arrays;

public class BatchState extends State {

    private final State[] states;

    public BatchState(State... states) {
        this.states = states;
    }

    @Override
    public void set(final Renderer renderer) {
        // System.out.println("set -> " + this);
        for (int i=0; i<states.length; i++) {
            states[i].set(renderer);
        }
    }

    @Override
    public void clear(final Renderer renderer) {
        // System.out.println("clear -> " + this);
        for (int i=0; i<states.length; i++) {
            states[i].clear(renderer);
        }
    }

    @Override
    public String toString() {
        return String.format("<%s %s>", getClass().getSimpleName(), Arrays.toString(states));
    }
}
