package org.quuux.opengl;

import com.jogamp.opengl.GL4;

public interface Renderable {
    void initialize(GL4 gl);
    void dispose(GL4 gl);
    void draw(GL4 gl, Camera camera);
}
