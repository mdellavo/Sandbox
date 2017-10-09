package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

public abstract class State extends CommandList implements Command {
    @Override
    public void run(GL gl) {
        setState(gl);
        super.run(gl);
        clearState(gl);
    }

    public abstract void clearState(GL gl);

    public abstract void setState(GL gl);
}
