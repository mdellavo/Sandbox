package org.quuux.opengl;

import com.jogamp.opengl.GL4;

import java.util.LinkedList;
import java.util.List;

public class EntityGroup implements Entity {

    private List<Entity> children = new LinkedList<>();

    @Override
    public void update(long t) {
        for(Entity child : this.children)
            child.update(t);
    }

    @Override
    public void initialize(GL4 gl) {
        for(Entity child : this.children)
            child.initialize(gl);
    }

    @Override
    public void dispose(GL4 gl) {
        for (Entity child : this.children)
            child.dispose(gl);
    }

    @Override
    public void draw(GL4 gl, Camera camera) {
        for(Entity child : this.children)
            child.draw(gl, camera);
    }

    public void addChild(Entity child) {
        children.add(child);
    }
}
