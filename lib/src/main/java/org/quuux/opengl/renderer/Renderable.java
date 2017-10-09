package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

public interface Renderable {
    void dispose(GL gl);
    Command draw();
}
