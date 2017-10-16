package org.quuux.driver;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class JOGLRenderer implements Renderer {

    GL gl;

    private int getTarget(BufferData.Target target) {
        final int rv;
        if (target == BufferData.Target.ArrayBuffer)
            rv = GL.GL_ARRAY_BUFFER;
        else if (target == BufferData.Target.ElementArrayBuffer)
            rv = GL.GL_ELEMENT_ARRAY_BUFFER;
        else
            throw new UnsupportedException("Unknown target: " + target.toString());
        return rv;
    }

    private int getUsage(BufferData.Usage usage) {
        final int rv;
        if (usage == BufferData.Usage.StaticDraw)
            rv = GL.GL_STATIC_DRAW;
        else if (usage == BufferData.Usage.DynamicDraw)
            rv = GL.GL_DYNAMIC_DRAW;
        else if (usage == BufferData.Usage.StreamDraw)
            rv = GL4.GL_STREAM_DRAW;
        else
            throw new UnsupportedException("Unknown usage: " + usage.toString());
        return rv;
    }

    private int getUniformLocation(ShaderProgram shader, String name) {
        Integer location = shader.getUniformLocation(name);
        if (location == null) {
            location = gl.getGL4().glGetUniformLocation(shader.program, name);
            shader.setUniformLocation(name, location);
        }

        return location;
    }

    private int getType(VertexAttribPointer.Type type) {
        final int rv;
        if (type == VertexAttribPointer.Type.Float)
            rv = GL.GL_FLOAT;
        else
            throw new UnsupportedException("Unknown type: " + type.toString());
        return rv;
    }

    private int getMode(DrawArrays.Mode mode) {
        final int rv;
        if (mode == DrawArrays.Mode.Triangles)
            rv = GL.GL_TRIANGLES;
        else
            throw new UnsupportedException("Unknown mode: " + mode.toString());
        return rv;
    }

    private int getFormat(LoadTexture2D.Format format) {
        final int rv;
        if (format == LoadTexture2D.Format.RGBA)
            rv = GL.GL_RGBA;
        else if (format == LoadTexture2D.Format.SRGB_ALPHA)
            rv = GL.GL_SRGB_ALPHA;
        else
            throw new UnsupportedException("Unknown format: " + format.toString());
        return rv;
    }

    private int getFilter(LoadTexture2D.Filter filter) {
        final int rv;
        if (filter == LoadTexture2D.Filter.LINEAR)
            rv = GL.GL_LINEAR;
        else if (filter == LoadTexture2D.Filter.NEAREST)
            rv = GL.GL_NEAREST;
        else
            throw new UnsupportedException("Unknown filter: " + filter.toString());
        return rv;
    }

    private int getCapability(Enable.Capability capability) {
        final int rv;
        if (capability == Enable.Capability.DEPTH_TEST)
            rv = GL.GL_DEPTH_TEST;
        else if (capability == Enable.Capability.BLEND)
            rv = GL.GL_BLEND;
        else if (capability == Enable.Capability.MULTISAMPLE)
            rv = GL.GL_MULTISAMPLE;
        else if (capability == Enable.Capability.POINT_SIZE)
            rv = GL.GL_POINT_SIZE;
        else
            throw new UnsupportedException("Unknown capability: " + capability.toString());
        return rv;
    }

    @Override
    public void run(final BufferData command) {
        gl.glBufferData(getTarget(command.getTarget()), command.getSize(), command.getData(), getUsage(command.getUsage()));
    }

    @Override
    public void run(final Clear command) {
        gl.glClear(command.getMask());
    }

    @Override
    public void run(final DrawArrays command) {
        gl.glDrawArrays(getMode(command.getMode()), command.getFirst(), command.getCount());
    }

    @Override
    public void run(final SetUniformMatrix command) {
        gl.getGL4().glUniformMatrix4fv(getUniformLocation(command.getShader(), command.getAttribute()), command.getCount(), command.isTranspose(), command.getBuffer());
    }

    @Override
    public void run(final SetUniform command) {
        if (command.getType() == SetUniform.Type.INT)
            gl.getGL4().glUniform1i(getUniformLocation(command.getProgram(), command.getAttribute()), 0);
    }

    @Override
    public void run(final VertexAttribPointer command) {
        gl.getGL4().glVertexAttribPointer(command.getIndex(), command.getSize(), getType(command.getType()), command.isNormalized(), command.getStride(), command.getPointer());
    }

    @Override
    public void run(final EnableVertexAttribArray command) {
        gl.getGL4().glEnableVertexAttribArray(command.getIndex());
    }

    @Override
    public void run(ClearColor command) {
        gl.glClearColor(command.getR(), command.getG(), command.getB(), command.getA());
    }

    @Override
    public void run(BlendFunc command) {
        gl.glBlendFunc(command.getSfactor(), command.getDfactor());
    }

    @Override
    public void run(DepthFunc command) {
        gl.glDepthFunc(command.getDepthFunc());
    }

    @Override
    public void set(final ActivateTexture command) {
        gl.glActiveTexture(command.getTextureUnit());
    }

    @Override
    public void clear(final ActivateTexture command) {
        gl.glActiveTexture(0);
    }

    @Override
    public void set(final BindBuffer command) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, command.getVBO().vbo);
    }

    @Override
    public void clear(final BindBuffer command) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void set(final UseProgram command) {
        gl.getGL4().glUseProgram(command.getProgram().program);
    }

    @Override
    public void clear(final UseProgram command) {
        gl.getGL4().glUseProgram(0);
    }

    @Override
    public void set(final BindTexture command) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, command.getTexture().texture);
    }

    @Override
    public void clear(final BindTexture command) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    @Override
    public void set(final BindArray command) {
        gl.getGL4().glBindVertexArray(command.getVAO().vao);
    }

    @Override
    public void clear(final BindArray command) {
        gl.getGL4().glBindVertexArray(0);
    }

    @Override
    public void set(final BindFramebuffer command) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clear(final BindFramebuffer command) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void set(Enable command) {
        gl.glEnable(getCapability(command.getCapability()));
    }

    @Override
    public void clear(Enable command) {
        gl.glDisable(getCapability(command.getCapability()));
    }

    @Override
    public void run(final GenerateBuffer command) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, tmp);
        command.getVbo().vbo = tmp.get(0);
    }

    @Override
    public void run(final GenerateArray command) {
        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);
        gl.getGL4().glGenVertexArrays(1, tmp);
        command.getVao().vao = tmp.get(0);
    }

    @Override
    public void run(final GenerateTexture2D command) {
        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);
        gl.glGenTextures(1, buffer);
        command.getTexture().texture = buffer.get(0);
    }

    @Override
    public void run(final LoadTexture2D command) {
        if (command.getBuffer() != null)
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4);

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, getFormat(command.getInternalFormat()), command.getWidth(), command.getHeight(), 0, getFormat(command.getFormat()), GL.GL_UNSIGNED_BYTE, command.getBuffer());

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, getFilter(command.getMin()));
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, getFilter(command.getMag()));
    }

    @Override
    public void run(final GenerateFramebuffer command) {

        IntBuffer buffer = GLBuffers.newDirectIntBuffer(1);

        GL4 gl4 = gl.getGL4();
        gl4.glGenFramebuffers(1, buffer);
        command.getFramebuffer().fbo = buffer.get(0);
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, command.getFramebuffer().fbo);

        gl4.glGenRenderbuffers(1, buffer);
        command.getFramebuffer().rbo = buffer.get(0);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, command.getFramebuffer().rbo);
        gl4.glRenderbufferStorage(GL4.GL_RENDERBUFFER, GL4.GL_DEPTH24_STENCIL8, command.getFramebuffer().width, command.getFramebuffer().height);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, 0);

        gl4.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, command.getFramebuffer().rbo);
    }

    @Override
    public void run(final AttachFramebuffer command) {
        gl.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, command.getTexture().texture, 0);
    }

    @Override
    public void run(final CreateProgram command) {
        command.getProgram().program = gl.getGL4().glCreateProgram();
    }

    public String byteBufferToString(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }

    @Override
    public void run(final CompileShader command) {

        GL4 gl4 = gl.getGL4();

        int shader = gl4.glCreateShader(command.getShaderType());
        gl4.glShaderSource(shader, 1, new String[] {command.getShaderSource()}, null);
        gl4.glCompileShader(shader);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl4.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl4.glGetShaderInfoLog(shader, buffer.capacity(), null, buffer);
            System.out.println("Error compiling shader: " + byteBufferToString(buffer));
            return;
        }
        gl4.glAttachShader(command.getProgram().program, shader);
    }

    @Override
    public void run(final LinkProgram command) {
        GL4 gl4 = gl.getGL4();
        gl4.glLinkProgram(command.getProgram().program);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl4.glGetProgramiv(command.getProgram().program, GL4.GL_LINK_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl4.glGetProgramInfoLog(command.getProgram().program, buffer.capacity(), null, buffer);
            System.out.println("Error linking shader: " + byteBufferToString(buffer));
        }
    }
}
