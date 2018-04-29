package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

import java.nio.Buffer;

public class BufferData extends Command {

    public enum Usage {
        StaticDraw,
        DynamicDraw,
        StreamDraw,
    }

    public enum Target {
        ArrayBuffer,
        ElementArrayBuffer,
    }

    private Target target;
    private int size;
    private Usage usage;
    private Buffer data;

    public BufferData(Target target, int size, Buffer data, Usage usage) {
        this.target = target;
        this.size = size;
        this.data = data;
        this.usage = usage;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public Target getTarget() {
        return target;
    }

    public int getSize() {
        return size;
    }

    public Usage getUsage() {
        return usage;
    }

    public Buffer getData() {
        return data;
    }

}
