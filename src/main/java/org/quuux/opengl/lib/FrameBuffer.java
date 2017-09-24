package org.quuux.opengl.lib;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.quuux.opengl.renderer.Bindable;

import java.nio.IntBuffer;

public class FrameBuffer implements Bindable {

    int fbo;
    int rbo;

    public FrameBuffer(GL gl, int width, int height) {
        //Log.out("*** Framebuffer init");

        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);

        GL4 gl4 = gl.getGL4();
        gl4.glGenFramebuffers(1, buffer);
        fbo = buffer.get(0);
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, fbo);

        gl4.glGenRenderbuffers(1, buffer);
        rbo = buffer.get(0);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, rbo);
        gl4.glRenderbufferStorage(GL4.GL_RENDERBUFFER, GL4.GL_DEPTH24_STENCIL8, width, height);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, 0);

        gl4.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, rbo);
    }

    public void bind(GL gl) {
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, fbo);
    }

    public void clear(GL gl) {
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
    }

    public boolean checkStatus(GL4 gl) {
        return gl.glCheckFramebufferStatus(GL4.GL_FRAMEBUFFER) == GL4.GL_FRAMEBUFFER_COMPLETE;
    }

    public void attach(GL4 gl, Texture2D texture) {
        gl.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, texture.texture, 0);
    }
}
