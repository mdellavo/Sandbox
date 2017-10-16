package org.quuux.opengl.renderer;

public interface Renderable {
    Command initialize();
    Command dispose();
    Command draw();
}
