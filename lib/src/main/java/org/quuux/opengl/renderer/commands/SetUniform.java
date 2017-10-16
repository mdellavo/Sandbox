package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;


public class SetUniform implements Command {

    public enum Type {
        INT,
    }

    private final ShaderProgram program;
    private final String attribute;
    private final Type type;
    private final Object value;

    public SetUniform(ShaderProgram program, String attribute, Type type, Object value) {
        this.program = program;
        this.attribute = attribute;
        this.type = type;
        this.value = value;
    }

    public SetUniform(ShaderProgram program, String name, int value) {
        this(program, name, Type.INT, value);
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

    public Object getValue() {
        return value;
    }

}
