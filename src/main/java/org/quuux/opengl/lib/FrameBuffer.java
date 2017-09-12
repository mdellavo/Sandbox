package org.quuux.opengl.lib;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.quuux.opengl.util.Log;

import java.nio.IntBuffer;

public class FrameBuffer {

    int fbo;
    int rbo;

    public FrameBuffer(GL4 gl, int width, int height) {
        //Log.out("*** Framebuffer init");

        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);

        gl.glGenFramebuffers(1, buffer);
        fbo = buffer.get(0);
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, fbo);

        gl.glGenRenderbuffers(1, buffer);
        rbo = buffer.get(0);
        gl.glBindRenderbuffer(GL4.GL_RENDERBUFFER, rbo);
        gl.glRenderbufferStorage(GL4.GL_RENDERBUFFER, GL4.GL_DEPTH24_STENCIL8, width, height);
        gl.glBindRenderbuffer(GL4.GL_RENDERBUFFER, 0);

        gl.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, rbo);
    }

    public void bind(GL4 gl) {
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, fbo);
    }

    public void clear(GL4 gl) {
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
    }

    public boolean checkStatus(GL4 gl) {
        return gl.glCheckFramebufferStatus(GL4.GL_FRAMEBUFFER) == GL4.GL_FRAMEBUFFER_COMPLETE;
    }

    public void attach(GL4 gl, Texture texture) {
        gl.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, texture.texture, 0);
    }
}
