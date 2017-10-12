package org.quuux.opengl.renderer;

import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.Clear;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BindVertex;
import org.quuux.opengl.renderer.states.Blend;
import org.quuux.opengl.renderer.states.Depth;
import org.quuux.opengl.renderer.states.UseProgram;

public interface Renderer {
    void run(BufferData command);
    void run(Clear command);
    void run(DrawArrays command);
    void run(SetUniformMatrix command);
    void run(ActivateTexture command);
    void run(BindVertex command);
    void run(Blend command);
    void run(Depth command);
    void run(UseProgram command);
}
