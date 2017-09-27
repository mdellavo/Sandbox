package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.State;

import java.nio.FloatBuffer;

public class SetUniformMatrix extends State {

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
    public void clearState(GL gl) {

    }

    @Override
    public void setState(GL gl) {
        gl.getGL4().glUniformMatrix4fv(shader.getUniformLocation(gl, attribute), count, transpose, buffer);
    }
}
