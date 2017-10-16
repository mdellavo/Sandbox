package org.quuux.opengl.entities;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;

import java.util.LinkedList;

public class EntityGroup extends LinkedList<Entity> implements Entity {
    @Override
    public void update(long t) {
        for(int i=0; i<size(); i++)
            get(i).update(t);
    }

    @Override
    public Command initialize() {
        CommandList commands = new CommandList();
        for(int i=0; i<size(); i++)
            commands.add(get(i).initialize());
        return commands;
    }

    @Override
    public Command dispose() {
        CommandList commands = new CommandList();
        for(int i=0; i<size(); i++)
            commands.add(get(i).dispose());
        return commands;
    }

    // FIXME very inefficient
    @Override
    public Command draw() {
        CommandList commands = new CommandList();
        for(int i=0; i<size(); i++)
            commands.add(get(i).draw());
        return commands;
    }
}
