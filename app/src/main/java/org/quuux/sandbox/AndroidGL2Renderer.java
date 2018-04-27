package org.quuux.sandbox;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES32;
import android.os.Build;

import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.Clear;
import org.quuux.opengl.renderer.commands.ClearColor;
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
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.BindFramebuffer;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.BlendFunc;
import org.quuux.opengl.renderer.states.DepthFunc;
import org.quuux.opengl.renderer.states.Enable;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.util.GLUtil;

import java.nio.IntBuffer;


class AndroidGL2Renderer implements Renderer {

    private int getTarget(final BufferData.Target target) {
        final int rv;
        if (target == BufferData.Target.ArrayBuffer)
            rv = GLES20.GL_ARRAY_BUFFER;
        else if (target == BufferData.Target.ElementArrayBuffer)
            rv = GLES20.GL_ELEMENT_ARRAY_BUFFER;
        else
            throw new UnsupportedException("Unknown target: " + target);
        return rv;
    }

    private int getUsage(BufferData.Usage usage) {
        final int rv;
        if (usage == BufferData.Usage.StaticDraw)
            rv = GLES20.GL_STATIC_DRAW;
        else if (usage == BufferData.Usage.DynamicDraw)
            rv = GLES20.GL_DYNAMIC_DRAW;
        else if (usage == BufferData.Usage.StreamDraw)
            rv = GLES20.GL_STREAM_DRAW;
        else
            throw new UnsupportedException("Unknown usage: " + usage);
        return rv;
    }

    private int getMaskValue(final Clear.Mode mode) {
        final int rv;
        if (mode == Clear.Mode.DEPTH_BUFFER)
            rv = GLES20.GL_DEPTH_BUFFER_BIT;
        else if (mode == Clear.Mode.COLOR_BUFFER)
            rv = GLES20.GL_COLOR_BUFFER_BIT;
        else
            throw new UnsupportedException("Unknown mode: " + mode);
        return rv;
    }

    private int getMask(final Clear.Mode[] modes) {
        int rv = 0;
        for (int i=0; i<modes.length; i++) {
            rv |= getMaskValue(modes[i]);
        }
        return rv;
    }

    private int getShaderType(final CompileShader.ShaderType type) {
        final int rv;
        if (type == CompileShader.ShaderType.FRAGMENT)
            rv = GLES20.GL_FRAGMENT_SHADER;
        else if (type == CompileShader.ShaderType.VERTEX)
            rv = GLES20.GL_VERTEX_SHADER;
        else
            throw new UnsupportedException("Unknown shader type: " + type);
        return rv;
    }

    private int getMode(DrawArrays.Mode mode) {
        final int rv;
        if (mode == DrawArrays.Mode.Points)
            rv = GLES20.GL_POINTS;
        else if (mode == DrawArrays.Mode.Triangles)
            rv = GLES20.GL_TRIANGLES;
        else
            throw new UnsupportedException("Unknown mode: " + mode);
        return rv;
    }

    private int getFormat(LoadTexture2D.Format format) {
        final int rv;
        if (format == LoadTexture2D.Format.RGBA)
            rv = GLES20.GL_RGBA;
        else if (format == LoadTexture2D.Format.RGB)
            rv = GLES20.GL_RGB;
        else if (format == LoadTexture2D.Format.RGBA16F)
            rv = GLES30.GL_RGBA16F;
        else
            throw new UnsupportedException("Unknown format: " + format.toString());
        return rv;
    }

    private int getFilter(LoadTexture2D.Filter filter) {
        final int rv;
        if (filter == LoadTexture2D.Filter.LINEAR)
            rv = GLES20.GL_LINEAR;
        else if (filter == LoadTexture2D.Filter.NEAREST)
            rv = GLES20.GL_NEAREST;
        else
            throw new UnsupportedException("Unknown filter: " + filter.toString());
        return rv;
    }

    private int getUniformLocation(ShaderProgram shader, String name) {
        Integer location = shader.getUniformLocation(name);
        if (location == null) {
            location = GLES20.glGetUniformLocation(shader.program, name);
            shader.setUniformLocation(name, location);
        }

        return location;
    }

    private int getType(VertexAttribPointer.Type type) {
        final int rv;
        if (type == VertexAttribPointer.Type.Float)
            rv = GLES20.GL_FLOAT;
        else
            throw new UnsupportedException("Unknown type: " + type);
        return rv;
    }

    private int getFactor(BlendFunc.Factor factor) {
        final int rv;
        if (factor == BlendFunc.Factor.SRC_ALPHA)
            rv = GLES20.GL_SRC_ALPHA;
        else if (factor == BlendFunc.Factor.ONE_MINUS_SRC_ALPHA)
            rv = GLES20.GL_ONE_MINUS_SRC_ALPHA;
        else
            throw new UnsupportedException("Unknown factor: " + factor);
        return rv;
    }

    private int getDepthFunc(DepthFunc.Function depthFunc) {
        final int rv;
        if (depthFunc == DepthFunc.Function.LESS)
            rv = GLES20.GL_LESS;
        else
            throw new UnsupportedException("Unknown depth function: " + depthFunc);
        return rv;
    }

    private int getTextureUnit(int value) {
        final int rv;
        if (value == 0)
            rv = GLES20.GL_TEXTURE0;
        else
            throw new UnsupportedException("Unknown texture unit: " + value);
        return rv;
    }

    private int getCapability(Enable.Capability capability) {
        final int rv;
        if (capability == Enable.Capability.DEPTH_TEST)
            rv = GLES20.GL_DEPTH_TEST;
        else if (capability == Enable.Capability.BLEND)
            rv = GLES20.GL_BLEND;
        else
            throw new UnsupportedException("Unknown capability: " + capability);
        return rv;
    }

    @Override
    public void run(final BufferData command) {
        GLES20.glBufferData(getTarget(command.getTarget()), command.getSize(), command.getData(), getUsage(command.getUsage()));
    }

    @Override
    public void run(final Clear command) {
        GLES20.glClear(getMask(command.getModes()));
    }

    @Override
    public void run(final CompileShader command) {
        int shader = GLES20.glCreateShader(getShaderType(command.getShaderType()));
        GLES20.glShaderSource(shader, command.getShaderSource("330 core"));
        GLES20.glCompileShader(shader);

        IntBuffer success = GLUtil.intBuffer(1);
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, success);
        if (success.get() == 0) {
            String info = GLES20.glGetShaderInfoLog(shader);
            throw new RendererException("Error compiling shader: " + info);
        }
        GLES20.glAttachShader(command.getProgram().program, shader);
    }

    @Override
    public void run(final CreateProgram command) {
        command.getProgram().program = GLES20.glCreateProgram();
    }

    @Override
    public void run(final DrawArrays command) {
        GLES20.glDrawArrays(getMode(command.getMode()), command.getFirst(), command.getCount());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void run(final GenerateArray command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        GLES30.glGenVertexArrays(1, tmp);
        command.getVao().vao = tmp.get();
    }

    @Override
    public void run(final GenerateBuffer command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        GLES30.glGenBuffers(1, tmp);
        command.getVbo().vbo = tmp.get();
    }

    @Override
    public void run(final GenerateFramebuffer command) {

    }

    @Override
    public void run(final GenerateTexture2D command) {
        IntBuffer buffer = GLUtil.intBuffer(1);
        GLES20.glGenTextures(1, buffer);
        command.getTexture().texture = buffer.get();
    }

    @Override
    public void run(final LinkProgram command) {
        GLES20.glLinkProgram(command.getProgram().program);
        IntBuffer success = GLUtil.intBuffer(1);
        GLES20.glGetProgramiv(command.getProgram().program, GLES20.GL_LINK_STATUS, success);
        if (success.get() == 0) {
            String info = GLES20.glGetProgramInfoLog(command.getProgram().program);
            throw new RendererException("Error linking shader: " + info);
        }
    }

    @Override
    public void run(final LoadTexture2D command) {
        if (command.getBuffer() != null)
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 4);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, getFormat(command.getInternalFormat()), command.getWidth(), command.getHeight(), 0, getFormat(command.getFormat()), GLES20.GL_UNSIGNED_BYTE, command.getBuffer());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, getFilter(command.getMin()));
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, getFilter(command.getMag()));
    }

    @Override
    public void run(final SetUniformMatrix command) {
        GLES20.glUniformMatrix4fv(getUniformLocation(command.getShader(), command.getAttribute()), command.getCount(), command.isTranspose(), command.getBuffer());
    }

    @Override
    public void run(final SetUniform command) {
        if (command.getType() == SetUniform.Type.INT)
            GLES20.glUniform1i(getUniformLocation(command.getProgram(), command.getAttribute()), 0);

    }

    @Override
    public void run(final VertexAttribPointer command) {
        GLES20.glVertexAttribPointer(command.getIndex(), command.getSize(), getType(command.getType()), command.isNormalized(), command.getStride(), command.getPointer());
    }

    @Override
    public void run(final EnableVertexAttribArray command) {
        GLES20.glEnableVertexAttribArray(command.getIndex());
    }

    @Override
    public void run(final ClearColor command) {
        GLES20.glClearColor(command.getR(), command.getG(), command.getB(), command.getA());
    }

    @Override
    public void run(final BlendFunc command) {
        GLES20.glBlendFunc(getFactor(command.getSfactor()), getFactor(command.getDfactor()));
    }

    @Override
    public void run(final DepthFunc command) {
        GLES20.glDepthFunc(getDepthFunc(command.getDepthFunc()));
    }

    @Override
    public void set(final ActivateTexture command) {
        GLES20.glActiveTexture(getTextureUnit(command.getTextureUnit()));
    }

    @Override
    public void clear(final ActivateTexture command) {

    }

    @Override
    public void set(final BindBuffer command) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, command.getVBO().vbo);
    }

    @Override
    public void clear(final BindBuffer command) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void set(final BindFramebuffer command) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, command.getFramebuffer().fbo);
    }

    @Override
    public void clear(final BindFramebuffer command) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void set(final Enable command) {
        Enable.Capability cap = command.getCapability();
        if (cap == Enable.Capability.MULTISAMPLE || cap == Enable.Capability.POINT_SIZE)
            return;
        GLES20.glEnable(getCapability(cap));
    }

    @Override
    public void clear(final Enable command) {
        Enable.Capability cap = command.getCapability();
        if (cap == Enable.Capability.MULTISAMPLE || cap == Enable.Capability.POINT_SIZE)
            return;
        GLES20.glDisable(getCapability(cap));
    }

    @Override
    public void set(final UseProgram command) {
        GLES20.glUseProgram(command.getProgram().program);
    }

    @Override
    public void clear(final UseProgram command) {
        GLES20.glUseProgram(0);
    }

    @Override
    public void set(final BindTexture command) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, command.getTexture().texture);
    }

    @Override
    public void clear(final BindTexture command) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void set(final BindArray command) {
        GLES30.glBindVertexArray(command.getVAO().vao);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void clear(final BindArray command) {
        GLES30.glBindVertexArray(0);
    }
}
