package org.quuux.opengl.lib;

public class FrameBuffer {

    public int width;
    public int height;
    public int fbo = -1;
    public int rbo = -1;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
