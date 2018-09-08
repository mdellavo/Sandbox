package org.quuux.opengl.entities;

import org.quuux.opengl.lib.Cubemap;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;

public class Skybox implements Entity {

    final String key;
    final Cubemap cubemap;

    public Skybox(String key) {
        this.key = key;
        cubemap = Cubemap.load(key);
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(cubemap.initialize(0));
        return rv;
    }

    @Override
    public Command dispose() {
        CommandList rv = new CommandList();
        return rv;
    }

    @Override
    public Command draw() {
        CommandList rv = new CommandList();
        rv.add(cubemap.bind(0));
        return rv;
    }

    @Override
    public void update(final long t) {

    }

}
