package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;

import java.nio.FloatBuffer;

public class SetUniformMatrix extends Command {

    private final ShaderProgram shader;
    private final String attribute;
    private final int count;
    private final boolean transpose;
    private final FloatBuffer buffer;

    public SetUniformMatrix(ShaderProgram shader, String attribute, int count, boolean transpose, FloatBuffer buffer) {
        this.shader = shader;
        this.attribute = attribute;
        this.count = count;
        this.transpose = transpose;
        this.buffer = buffer;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getCount() {
        return count;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }
}
