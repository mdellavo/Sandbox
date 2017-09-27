package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;
import org.quuux.opengl.renderer.Command;

import java.nio.Buffer;

public class BufferData implements Command {
    int target, size, usage;
    Buffer data;

    public BufferData(int target, int size, Buffer data, int usage) {
        this.target = target;
        this.size = size;
        this.data = data;
        this.usage = usage;
    }

    @Override
    public void run(GL gl) {
        gl.glBufferData(target, size, data, usage);
    }
}
