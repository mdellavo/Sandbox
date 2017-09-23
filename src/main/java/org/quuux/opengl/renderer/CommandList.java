package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements Command {
    public List<Command> commands = new ArrayList<>();

    @Override
    public void run(GL gl) {
        for(int i=0; i<commands.size(); i++)
            commands.get(i).run(gl);
    }
}
