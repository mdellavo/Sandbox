package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;
import org.quuux.opengl.renderer.Command;

public class Clear implements Command {

    private final int mask;

    public Clear(int mask) {
        this.mask = mask;
    }

    @Override
    public void run(GL gl) {
        gl.glClear(mask);
    }
}
