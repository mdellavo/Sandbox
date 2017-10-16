package org.quuux.opengl.lib;

public class FrameBuffer {

    public int width;
    public int height;
    public int fbo;
    public int rbo;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
