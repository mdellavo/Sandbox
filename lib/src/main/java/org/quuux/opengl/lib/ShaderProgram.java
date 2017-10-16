package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.commands.CompileShader;
import org.quuux.opengl.renderer.commands.CreateProgram;
import org.quuux.opengl.renderer.commands.LinkProgram;

import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {

    public int program = 0;

    Map<String, Integer> uniformCache = new HashMap<>();

    public Integer getUniformLocation(String name) {
        return uniformCache.get(name);
    }

    public void setUniformLocation(String name, int location) {
        uniformCache.put(name, location);
    }

    public static Command build(ShaderProgram program, final String vertexShader, final String fragmentShader) {

        CommandList rv = new CommandList();
        rv.add(new CreateProgram(program));
        rv.add(new CompileShader(program, 0, vertexShader));
        rv.add(new CompileShader(program, 0, fragmentShader));
        rv.add(new LinkProgram(program));

        return rv;
    }
}
