package org.quuux.opengl.renderer;

import java.util.ArrayList;

public class CommandList extends ArrayList<Command> implements Command {
    @Override
    public void run(final Renderer renderer) {
        for (int i=0; i<size(); i++) {
            get(i).run(renderer);
        }
    }
}
