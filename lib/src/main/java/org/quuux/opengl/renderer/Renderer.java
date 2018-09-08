package org.quuux.opengl.renderer;

import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;

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

    void checkError();

    // Commands
    void run(BufferData command);
    void run(Clear command);
    void run(CompileShader command);
    void run(CreateProgram command);
    void run(DrawArrays command);
    void run(DrawElements command);
    void run(GenerateArray command);
    void run(GenerateBuffer command);
    void run(GenerateFramebuffer command);
    void run(GenerateTexture command);
    void run(LinkProgram command);
    void run(LoadTexture command);
    void run(SetUniformMatrix command);
    void run(SetUniform command);
    void run(VertexAttribPointer command);
    void run(EnableVertexAttribArray command);
    void run(ClearColor command);
    void run(BlendFunc command);
    void run(DepthFunc command);
    void run(TextureParameter command);
    void run(GenerateMipMap command);

    // States
    void set(ActivateTexture command);
    void clear(ActivateTexture command);

    void set(BindBuffer command);
    void clear(BindBuffer command);

    void set(BindFramebuffer command);
    void clear(BindFramebuffer commJOGLand);

    void set(Enable command);
    void clear(Enable command);

    void set(UseProgram command);
    void clear(UseProgram command);

    void set(BindTexture command);
    void clear(BindTexture command);

    void set(BindArray command);
    void clear(BindArray command);
}
