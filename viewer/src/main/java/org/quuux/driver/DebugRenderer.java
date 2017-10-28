package org.quuux.driver;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;

public class DebugRenderer extends JOGLRenderer {
    private void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private void log(Command command) {
        log(command.getClass().getSimpleName());
    }

    private void logSet(Command command) {
        log(">>> %s", command.getClass().getSimpleName());
    }

    private void logClear(Command command) {
        log("<<< %s", command.getClass().getSimpleName());
    }

    @Override
    public void run(BufferData command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(Clear command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(CompileShader command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(CreateProgram command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(DrawArrays command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(GenerateArray command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(GenerateBuffer command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(GenerateFramebuffer command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(GenerateTexture2D command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(LinkProgram command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(LoadTexture2D command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(SetUniformMatrix command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(SetUniform command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(VertexAttribPointer command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(EnableVertexAttribArray command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(ClearColor command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(BlendFunc command) {
        log(command);
        super.run(command);
    }

    @Override
    public void run(DepthFunc command) {
        log(command);
        super.run(command);
    }

    @Override
    public void set(ActivateTexture command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(ActivateTexture command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(BindBuffer command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(BindBuffer command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(BindFramebuffer command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(BindFramebuffer command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(Enable command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(Enable command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(UseProgram command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(UseProgram command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(BindTexture command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(BindTexture command) {
        logClear(command);
        super.clear(command);
    }

    @Override
    public void set(BindArray command) {
        logSet(command);
        super.set(command);
    }

    @Override
    public void clear(BindArray command) {
        logClear(command);
        super.clear(command);
    }
}
