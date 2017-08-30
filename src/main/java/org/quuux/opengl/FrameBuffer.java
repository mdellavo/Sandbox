package org.quuux.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.IntBuffer;

public class FrameBuffer {

    int framebuffer;
    int renderbuffer;

    FrameBuffer(GL4 gl) {
        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);

        gl.glGenFramebuffers(1, buffer);
        framebuffer = buffer.get(0);

        gl.glGenRenderbuffers(1, buffer);
        renderbuffer = buffer.get(0);
    }

    public void bind(GL4 gl) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebuffer);
    }

    public void attach(GL4 gl, Texture texture) {

    }
}
