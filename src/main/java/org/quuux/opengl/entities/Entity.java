package org.quuux.opengl.entities;

import org.quuux.opengl.renderer.Renderable;

public interface Entity extends Renderable {
    void update(long t);
}
