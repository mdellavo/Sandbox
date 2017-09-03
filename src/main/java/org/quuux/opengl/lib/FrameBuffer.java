package org.quuux.opengl.lib;

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
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, framebuffer);
        gl.glBindRenderbuffer(GL4.GL_RENDERBUFFER, renderbuffer);
    }

    public void attach(GL4 gl, Texture texture, int width, int height) {
        gl.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, texture.texture, 0);
        gl.glRenderbufferStorage(GL4.GL_RENDERBUFFER, GL4.GL_DEPTH24_STENCIL8, width, height);
        gl.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, renderbuffer);
    }
}
