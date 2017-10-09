package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;

import org.quuux.opengl.renderer.Command;

public class DrawArrays implements Command {

    private final int mode;
    private final int first;
    private final int count;

    public DrawArrays(int mode, int first, int count) {
        this.mode = mode;
        this.first = first;
        this.count = count;
    }

    @Override
    public void run(GL gl) {
        gl.glDrawArrays(mode, first, count);
    }
}
