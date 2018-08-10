package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.Renderer;


public abstract class State extends CommandList {
    public abstract void set(Renderer renderer);
    public abstract void clear(Renderer renderer);

    @Override
    public void run(Renderer renderer) {
        set(renderer);
        super.run(renderer);
        clear(renderer);
    }

}
