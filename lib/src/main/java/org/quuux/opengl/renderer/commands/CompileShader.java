package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;


public class CompileShader extends Command {

    public enum ShaderType {
        VERTEX,
        FRAGMENT
    }

    private final ShaderProgram program;
    private final ShaderType shaderType;
    private final String shaderSource;

    public CompileShader(ShaderProgram program, ShaderType shaderType, String shaderSource) {
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

    public ShaderType getShaderType() {
        return shaderType;
    }

    public String getShaderSource(String version) {
        return "#version " + version + "\n" + shaderSource;
    }
}
