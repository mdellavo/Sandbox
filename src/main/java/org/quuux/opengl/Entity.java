package org.quuux.opengl;

import com.jogamp.opengl.GL4;

public interface Entity extends Renderable {
    void update(long t);
}
