package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.states.State;

public abstract class Texture {
    public final int unit;
    public int texture = -1;
    public String key;

    public Texture(String key, int unit) {
        this.key = key;
        this.unit = unit;
    }

    public abstract Command initialize();
    public abstract State bind();

}
