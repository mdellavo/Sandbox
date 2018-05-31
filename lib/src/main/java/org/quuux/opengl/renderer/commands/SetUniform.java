package org.quuux.opengl.renderer.commands;

import org.joml.Vector3f;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;


public class SetUniform extends Command {

    public enum Type {
        INT,
        FLOAT,
    }

    private final ShaderProgram program;
    private final String attribute;
    private final Type type;
    private final Object[] value;

    public SetUniform(ShaderProgram program, String attribute, Type type, Object... value) {
        this.program = program;
        this.attribute = attribute;
        this.type = type;
        this.value = value;
    }

    public SetUniform(ShaderProgram program, String name, int value) {
        this(program, name, Type.INT, value);
    }

    public SetUniform(ShaderProgram program, String name, int v1, int v2) {
        this(program, name, Type.INT, v1, v2);
    }

    public SetUniform(ShaderProgram program, String name, int v1, int v2, int v3) {
        this(program, name, Type.INT, v1, v2, v3);
    }

    public SetUniform(ShaderProgram program, String name, float value) {
        this(program, name, Type.FLOAT, value);
    }

    public SetUniform(ShaderProgram program, String name, float v1, float v2) {
        this(program, name, Type.FLOAT, v1, v2);
    }

    public SetUniform(ShaderProgram program, String name, float v1, float v2, float v3) {
        this(program, name, Type.FLOAT, v1, v2, v3);
    }

    public SetUniform(ShaderProgram program, String name, Vector3f vec) {
        this(program, name, vec.x, vec.y, vec.z);
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public ShaderProgram getProgram() {
        return program;
    }

    public String getAttribute() {
        return attribute;
    }

    public Type getType() {
        return type;
    }

    public Object[] getValue() {
        return value;
    }

}
