package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.Renderer;


public abstract class State extends CommandList {
    abstract void set(Renderer renderer);
    abstract void clear(Renderer renderer);
}
