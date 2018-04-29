package org.quuux.opengl.renderer;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandList extends Command {

    ArrayList<Command> commands = new ArrayList<>();

    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public void run(final Renderer renderer) {
        // System.out.println("run: " + Arrays.toString(commands.toArray()));

        for (int i=0; i<commands.size(); i++) {
            Command command = commands.get(i);
            command.run(renderer);
            renderer.checkError();
        }
    }
}
