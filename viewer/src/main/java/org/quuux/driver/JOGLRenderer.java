package org.quuux.driver;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;

import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;
import org.quuux.opengl.util.GLUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class JOGLRenderer implements Renderer {

    GL _gl;

    public void setGL(GL gl) {
        this._gl = gl;
    }

    public GL4 getGL() {
        return _gl.getGL4();
    }

    private int getTarget(BufferType target) {
        final int rv;
        if (target == BufferType.ArrayBuffer)
            rv = GL.GL_ARRAY_BUFFER;
        else if (target == BufferType.ElementArrayBuffer)
            rv = GL.GL_ELEMENT_ARRAY_BUFFER;
        else
            throw new UnsupportedException("Unknown target: " + target);
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
            throw new UnsupportedException("Unknown usage: " + usage);
        return rv;
    }

    private int getUniformLocation(ShaderProgram shader, String name) {
        Integer location = shader.getUniformLocation(name);
        if (location == null) {
            location = getGL().glGetUniformLocation(shader.program, name);
            shader.setUniformLocation(name, location);
        }

        return location;
    }

    private int getType(VertexAttribPointer.Type type) {
        final int rv;
        if (type == VertexAttribPointer.Type.Float)
            rv = GL.GL_FLOAT;
        else
            throw new UnsupportedException("Unknown type: " + type);
        return rv;
    }

    private int getMode(DrawMode mode) {
        final int rv;
        if (mode == DrawMode.Triangles)
            rv = GL.GL_TRIANGLES;
        else if(mode == DrawMode.Points)
            rv = GL.GL_POINTS;
        else
            throw new UnsupportedException("Unknown mode: " + mode);
        return rv;
    }

    private int getFormat(LoadTexture2D.Format format) {
        final int rv;
        if (format == LoadTexture2D.Format.RGBA)
            rv = GL.GL_RGBA;
        else if (format == LoadTexture2D.Format.RGB)
            rv = GL.GL_RGB;
        else if (format == LoadTexture2D.Format.SRGB_ALPHA)
            rv = GL.GL_SRGB_ALPHA;
        else if (format == LoadTexture2D.Format.RGBA16F)
            rv = GL.GL_RGBA16F;
        else
            throw new UnsupportedException("Unknown format: " + format);
        return rv;
    }

    private int getFilter(LoadTexture2D.Filter filter) {
        final int rv;
        if (filter == LoadTexture2D.Filter.LINEAR)
            rv = GL.GL_LINEAR;
        else if (filter == LoadTexture2D.Filter.NEAREST)
            rv = GL.GL_NEAREST;
        else
            throw new UnsupportedException("Unknown filter: " + filter);
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
            rv = GL4.GL_PROGRAM_POINT_SIZE;
        else
            throw new UnsupportedException("Unknown capability: " + capability);
        return rv;
    }

    private int getMask(Clear.Mode[] modes) {
        int rv = 0;
        for (int i=0; i<modes.length; i++) {
            rv |= getMaskValue(modes[i]);
        }
        return rv;
    }

    private int getMaskValue(Clear.Mode mode) {
        final int rv;
        if (mode == Clear.Mode.COLOR_BUFFER)
            rv = GL.GL_COLOR_BUFFER_BIT;
        else if (mode == Clear.Mode.DEPTH_BUFFER)
            rv = GL.GL_DEPTH_BUFFER_BIT;
        else
            throw new UnsupportedException("Unknown mode: " + mode);
        return rv;
    }

    private int getFactor(BlendFunc.Factor factor) {
        final int rv;
        if (factor == BlendFunc.Factor.SRC_ALPHA)
            rv = GL.GL_SRC_ALPHA;
        else if (factor == BlendFunc.Factor.ONE_MINUS_SRC_ALPHA)
            rv = GL.GL_ONE_MINUS_SRC_ALPHA;
        else
            throw new UnsupportedException("Unknown factor: " + factor);
        return rv;
    }

    private int getDepthFunc(DepthFunc.Function depthFunc) {
        final int rv;
        if (depthFunc == DepthFunc.Function.LESS)
            rv = GL.GL_LESS;
        else
            throw new UnsupportedException("Unknown depth function: " + depthFunc);
        return rv;
    }

    private int getTextureUnit(int value) {
        final int rv;
        if (value == 0)
            rv = GL.GL_TEXTURE0;
        else
            throw new UnsupportedException("Unknown texture unit: " + value);
        return rv;
    }

    private int getShaderType(CompileShader.ShaderType type) {
        final int rv;
        if (type == CompileShader.ShaderType.FRAGMENT)
            rv = GL4.GL_FRAGMENT_SHADER;
        else if (type == CompileShader.ShaderType.VERTEX)
            rv = GL4.GL_VERTEX_SHADER;
        else
            throw new UnsupportedException("Unknown shader type: " + type);
        return rv;
    }

    public String byteBufferToString(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }

    public void checkError() {
    }

    @Override
    public void run(final BufferData command) {
        getGL().glBufferData(getTarget(command.getTarget()), command.getSize(), command.getData(), getUsage(command.getUsage()));
    }

    @Override
    public void run(final Clear command) {
        getGL().glClear(getMask(command.getModes()));
    }

    @Override
    public void run(final DrawArrays command) {
        getGL().glDrawArrays(getMode(command.getMode()), command.getFirst(), command.getCount());
    }

    @Override
    public void run(final DrawElements command) {
        getGL().glDrawElements(getMode(command.getMode()), command.getCount(), GL.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void run(final SetUniformMatrix command) {
        getGL().glUniformMatrix4fv(getUniformLocation(command.getShader(), command.getAttribute()), command.getCount(), command.isTranspose(), command.getBuffer());
    }

    @Override
    public void run(final SetUniform command) {
        if (command.getType() == SetUniform.Type.INT)
            getGL().glUniform1i(getUniformLocation(command.getProgram(), command.getAttribute()), 0);
    }

    @Override
    public void run(final VertexAttribPointer command) {
        getGL().glVertexAttribPointer(command.getIndex(), command.getSize(), getType(command.getType()), command.isNormalized(), command.getStride(), command.getPointer());
    }

    @Override
    public void run(final EnableVertexAttribArray command) {
        getGL().glEnableVertexAttribArray(command.getIndex());
    }

    @Override
    public void run(ClearColor command) {
        getGL().glClearColor(command.getR(), command.getG(), command.getB(), command.getA());
    }

    @Override
    public void run(BlendFunc command) {
        getGL().glBlendFunc(getFactor(command.getSfactor()), getFactor(command.getDfactor()));
    }

    @Override
    public void run(DepthFunc command) {
        getGL().glDepthFunc(getDepthFunc(command.getDepthFunc()));
    }

    @Override
    public void set(final ActivateTexture command) {
        getGL().glActiveTexture(getTextureUnit(command.getTextureUnit()));
    }

    @Override
    public void clear(final ActivateTexture command) {
    }

    @Override
    public void set(final BindBuffer command) {
        getGL().glBindBuffer(getTarget(command.getTarget()), command.getVBO().vbo);
    }

    @Override
    public void clear(final BindBuffer command) {
        getGL().glBindBuffer(getTarget(command.getTarget()), 0);
    }

    @Override
    public void set(final UseProgram command) {
        getGL().glUseProgram(command.getProgram().program);
    }

    @Override
    public void clear(final UseProgram command) {
        getGL().glUseProgram(0);
    }

    @Override
    public void set(final BindTexture command) {
        getGL().glBindTexture(GL.GL_TEXTURE_2D, command.getTexture().texture);
    }

    @Override
    public void clear(final BindTexture command) {
        getGL().glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    @Override
    public void set(final BindArray command) {
        getGL().glBindVertexArray(command.getVAO().vao);
    }

    @Override
    public void clear(final BindArray command) {
        getGL().glBindVertexArray(0);
    }

    @Override
    public void set(final BindFramebuffer command) {
        GL gl = getGL();
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, command.getFramebuffer().fbo);
    }

    @Override
    public void clear(final BindFramebuffer command) {
        getGL().glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void set(Enable command) {
        getGL().glEnable(getCapability(command.getCapability()));
    }

    @Override
    public void clear(Enable command) {
        getGL().glDisable(getCapability(command.getCapability()));
    }

    @Override
    public void run(final GenerateBuffer command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        getGL().glGenBuffers(1, tmp);
        command.getVbo().vbo = tmp.get();
    }

    @Override
    public void run(final GenerateArray command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        getGL().glGenVertexArrays(1, tmp);
        command.getVao().vao = tmp.get();
    }

    @Override
    public void run(final GenerateTexture2D command) {
        IntBuffer buffer = GLUtil.intBuffer(1);
        getGL().glGenTextures(1, buffer);
        command.getTexture().texture = buffer.get();
    }

    @Override
    public void run(final LoadTexture2D command) {
        GL gl = getGL();
        if (command.getBuffer() != null)
            getGL().glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4);

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, getFormat(command.getInternalFormat()), command.getWidth(), command.getHeight(), 0, getFormat(command.getFormat()), GL.GL_UNSIGNED_BYTE, command.getBuffer());

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, getFilter(command.getMin()));
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, getFilter(command.getMag()));
    }

    @Override
    public void run(final GenerateFramebuffer command) {

        FrameBuffer framebuffer = command.getFramebuffer();
        Texture2D texture = command.getTexture();

        IntBuffer buffer = GLUtil.intBuffer(1);

        GL4 gl4 = getGL();

        gl4.glGenFramebuffers(1, buffer);
        framebuffer.fbo = buffer.get();
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, framebuffer.fbo);

        gl4.glGenTextures(1, buffer);
        texture.texture = buffer.get();
        gl4.glBindTexture(GL.GL_TEXTURE_2D, texture.texture);
        gl4.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, framebuffer.width, framebuffer.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, null);
        gl4.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl4.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl4.glBindTexture(GL.GL_TEXTURE_2D, 0);

        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, texture.texture, 0);

        gl4.glGenRenderbuffers(1, buffer);
        framebuffer.rbo = buffer.get();
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, framebuffer.rbo);
        gl4.glRenderbufferStorage(GL4.GL_RENDERBUFFER, GL4.GL_DEPTH24_STENCIL8, framebuffer.width, framebuffer.height);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, 0);

        gl4.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, framebuffer.rbo);

        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void run(final CreateProgram command) {
        command.getProgram().program = getGL().getGL4().glCreateProgram();
    }

    @Override
    public void run(final CompileShader command) {

        GL4 gl4 = getGL();

        int shader = gl4.glCreateShader(getShaderType(command.getShaderType()));
        gl4.glShaderSource(shader, 1, new String[] {command.getShaderSource("410")}, null);
        gl4.glCompileShader(shader);

        IntBuffer success = GLUtil.intBuffer(1);
        gl4.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, success);
        if (success.get() == 0) {
            ByteBuffer buffer = GLUtil.byteBuffer(1024);
            gl4.glGetShaderInfoLog(shader, buffer.capacity(), null, buffer);
            throw new RendererException("Error compiling shader: " + byteBufferToString(buffer));
        }
        gl4.glAttachShader(command.getProgram().program, shader);
    }

    @Override
    public void run(final LinkProgram command) {
        GL4 gl4 = getGL();
        gl4.glLinkProgram(command.getProgram().program);

        IntBuffer success = GLUtil.intBuffer(1);
        gl4.glGetProgramiv(command.getProgram().program, GL4.GL_LINK_STATUS, success);
        if (success.get() == 0) {
            ByteBuffer buffer = GLUtil.byteBuffer(1024);
            gl4.glGetProgramInfoLog(command.getProgram().program, buffer.capacity(), null, buffer);
            throw new RendererException("Error linking shader: " + byteBufferToString(buffer));
        }
    }
}
