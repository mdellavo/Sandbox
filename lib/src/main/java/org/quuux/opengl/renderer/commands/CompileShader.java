package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;


public class CompileShader implements Command {

    private final ShaderProgram program;
    private final int shaderType;
    private final String shaderSource;

    public CompileShader(ShaderProgram program, int shaderType, String shaderSource) {
        this.program = program;
        this.shaderType = shaderType;
        this.shaderSource = shaderSource;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public ShaderProgram getProgram() {
        return program;
    }

    public int getShaderType() {
        return shaderType;
    }

    public String getShaderSource() {
        return shaderSource;
    }
}
