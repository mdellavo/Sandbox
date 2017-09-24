package org.quuux.opengl.entities;

import com.jogamp.opengl.GL;

import java.util.LinkedList;

public class EntityGroup extends LinkedList<Entity> implements Entity {
    @Override
    public void update(long t) {
        for(int i=0; i<size(); i++)
            get(i).update(t);
    }

    @Override
    public void dispose(GL gl) {
        for(int i=0; i<size(); i++)
            get(i).dispose(gl);
    }

    @Override
    public void draw(GL gl) {
        for(int i=0; i<size(); i++)
            get(i).draw(gl);
    }
}
