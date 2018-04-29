package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;


public class VertexAttribPointer extends Command {

    public enum Type {
        Float,
    }

    private final int index;
    private final int size;
    private final Type type;
    private final int stride;
    private final boolean normalized;
    private final int pointer;

    public VertexAttribPointer(int index, int size, Type type, boolean normalized, int stride, int pointer) {
        this.index = index;
        this.size = size;
        this.type = type;
        this.stride = stride;
        this.normalized = normalized;
        this.pointer = pointer;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }

    public int getStride() {
        return stride;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public int getPointer() {
        return pointer;
    }
}
