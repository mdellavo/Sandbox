package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;

import java.nio.FloatBuffer;

public class SetUniformMatrix implements Command {

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
    public void run(GL gl) {
        gl.getGL4().glUniformMatrix4fv(shader.getUniformLocation(gl, attribute), count, transpose, buffer);
    }
}
