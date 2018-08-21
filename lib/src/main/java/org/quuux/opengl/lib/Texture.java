package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.states.State;

public abstract class Texture {
    public int texture = -1;

    public abstract Command initialize(int unit);
    public abstract State bind(int unit);

}
