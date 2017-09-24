package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public interface Renderable {
    void dispose(GL gl);
    void draw(GL gl);
}
