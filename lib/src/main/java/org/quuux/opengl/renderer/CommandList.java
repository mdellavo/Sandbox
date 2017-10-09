package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

import java.util.ArrayList;

public class CommandList extends ArrayList<Command> implements Command {

    @Override
    public void run(GL gl) {
        for(int i=0; i<size(); i++)
            get(i).run(gl);
    }
}
