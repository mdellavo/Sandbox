package org.quuux.opengl.renderer.states;


import org.quuux.opengl.renderer.Renderer;

public class BatchState extends State {

    private final State[] states;

    public BatchState(State... states) {
        this.states = states;
    }

    @Override
    void set(final Renderer renderer) {
        for (int i=0; i<states.length; i++) {
            states[i].set(renderer);
        }
    }

    @Override
    void clear(final Renderer renderer) {
        for (int i=0; i<states.length; i++) {
            states[i].clear(renderer);
        }
    }
}
