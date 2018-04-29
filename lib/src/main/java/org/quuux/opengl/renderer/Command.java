package org.quuux.opengl.renderer;

public abstract class Command {
    public abstract void run(Renderer renderer);

    @Override
    public String toString() {
        return String.format("<%s>", getClass().getSimpleName());
    }
}
