package org.quuux.opengl.entities;

import com.jogamp.opengl.GL4;

import java.util.LinkedList;
import java.util.List;

public class EntityGroup extends LinkedList<Entity> implements Entity {
    @Override
    public void update(long t) {
        for(int i=0; i<size(); i++)
            get(i).update(t);
    }

    @Override
    public void dispose(GL4 gl) {
        for(int i=0; i<size(); i++)
            get(i).dispose(gl);
    }

    @Override
    public void draw(GL4 gl) {
        for(int i=0; i<size(); i++)
            get(i).draw(gl);
    }
}
