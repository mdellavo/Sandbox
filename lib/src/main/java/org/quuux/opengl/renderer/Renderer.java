package org.quuux.opengl.renderer;

import org.quuux.opengl.renderer.commands.AttachFramebuffer;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.Clear;
import org.quuux.opengl.renderer.commands.CompileShader;
import org.quuux.opengl.renderer.commands.CreateProgram;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.GenerateFramebuffer;
import org.quuux.opengl.renderer.commands.GenerateTexture2D;
import org.quuux.opengl.renderer.commands.LinkProgram;
import org.quuux.opengl.renderer.commands.LoadTexture2D;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindFramebuffer;
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.Blend;
import org.quuux.opengl.renderer.states.Depth;
import org.quuux.opengl.renderer.states.UseProgram;

public interface Renderer {

    class RendererException extends RuntimeException {
        public RendererException(String message) {
            super(message);
        }
    }
    class UnsupportedException extends RendererException {
        public UnsupportedException(String message) {
            super(message);
        }
    }

    // Commands
    void run(AttachFramebuffer command);
    void run(BufferData command);
    void run(Clear command);
    void run(CompileShader command);
    void run(CreateProgram command);
    void run(DrawArrays command);
    void run(GenerateArray command);
    void run(GenerateBuffer command);
    void run(GenerateFramebuffer command);
    void run(GenerateTexture2D command);
    void run(LinkProgram command);
    void run(LoadTexture2D command);
    void run(SetUniformMatrix command);
    void run(SetUniform command);
    void run(VertexAttribPointer command);
    void run(EnableVertexAttribArray command);


    // States
    void set(ActivateTexture command);
    void clear(ActivateTexture command);

    void set(BindBuffer command);
    void clear(BindBuffer command);

    void set(BindFramebuffer command);
    void clear(BindFramebuffer command);

    void set(Blend command);
    void clear(Blend command);

    void set(Depth command);
    void clear(Depth command);

    void set(UseProgram command);
    void clear(UseProgram command);

    void set(BindTexture command);
    void clear(BindTexture command);

    void set(BindArray command);
    void clear(BindArray command);
}
