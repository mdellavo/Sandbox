package org.quuux.opengl.renderer.commands;

import com.jogamp.opengl.GL;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;

public class UseProgram implements Command {
    private final ShaderProgram program;

    public UseProgram(ShaderProgram program) {
        this.program = program;
    }

    @Override
    public void run(GL gl) {
        program.use(gl.getGL4());
    }
}
