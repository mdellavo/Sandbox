package org.quuux.sandbox;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;

import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.Clear;
import org.quuux.opengl.renderer.commands.ClearColor;
import org.quuux.opengl.renderer.commands.CompileShader;
import org.quuux.opengl.renderer.commands.CreateProgram;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.DrawElements;
import org.quuux.opengl.renderer.commands.DrawMode;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.GenerateFramebuffer;
import org.quuux.opengl.renderer.commands.GenerateMipMap;
import org.quuux.opengl.renderer.commands.GenerateTexture;
import org.quuux.opengl.renderer.commands.LinkProgram;
import org.quuux.opengl.renderer.commands.LoadTexture;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.commands.TextureParameter;
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

import java.nio.Buffer;
import java.nio.IntBuffer;


class AndroidGLRenderer implements Renderer {

    private static final boolean DEBUG = false;

    private int getTarget(final BufferType target) {
        final int rv;
        if (target == BufferType.ArrayBuffer)
            rv = GLES30.GL_ARRAY_BUFFER;
        else if (target == BufferType.ElementArrayBuffer)
            rv = GLES30.GL_ELEMENT_ARRAY_BUFFER;
        else
            throw new UnsupportedException("Unknown target: " + target);
        return rv;
    }

    private int getUsage(BufferData.Usage usage) {
        final int rv;
        if (usage == BufferData.Usage.StaticDraw)
            rv = GLES30.GL_STATIC_DRAW;
        else if (usage == BufferData.Usage.DynamicDraw)
            rv = GLES30.GL_DYNAMIC_DRAW;
        else if (usage == BufferData.Usage.StreamDraw)
            rv = GLES30.GL_STREAM_DRAW;
        else
            throw new UnsupportedException("Unknown usage: " + usage);
        return rv;
    }

    private int getMaskValue(final Clear.Mode mode) {
        final int rv;
        if (mode == Clear.Mode.DEPTH_BUFFER)
            rv = GLES30.GL_DEPTH_BUFFER_BIT;
        else if (mode == Clear.Mode.COLOR_BUFFER)
            rv = GLES30.GL_COLOR_BUFFER_BIT;
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
            rv = GLES30.GL_FRAGMENT_SHADER;
        else if (type == CompileShader.ShaderType.VERTEX)
            rv = GLES30.GL_VERTEX_SHADER;
        else
            throw new UnsupportedException("Unknown shader type: " + type);
        return rv;
    }

    private int getMode(DrawMode mode) {
        final int rv;
        if (mode == DrawMode.Points)
            rv = GLES30.GL_POINTS;
        else if (mode == DrawMode.Triangles)
            rv = GLES30.GL_TRIANGLES;
        else
            throw new UnsupportedException("Unknown mode: " + mode);
        return rv;
    }

    private int getFormat(LoadTexture.Format format) {
        final int rv;
        if (format == LoadTexture.Format.RGBA)
            rv = GLES30.GL_RGBA;
        else if (format == LoadTexture.Format.RGB)
            rv = GLES30.GL_RGB;
        else if (format == LoadTexture.Format.RGBA16F)
            rv = GLES30.GL_RGBA16F;
        else
            throw new UnsupportedException("Unknown format: " + format.toString());
        return rv;
    }

    private int getFilter(TextureParameter.Filter filter) {
        final int rv;
        if (filter == TextureParameter.Filter.LINEAR)
            rv = GLES30.GL_LINEAR;
        else if (filter == TextureParameter.Filter.NEAREST)
            rv = GLES30.GL_NEAREST;
        else
            throw new UnsupportedException("Unknown filter: " + filter.toString());
        return rv;
    }

    private int getUniformLocation(ShaderProgram shader, String name) {
        Integer location = shader.getUniformLocation(name);
        checkError();
        if (location == null) {
            location = GLES30.glGetUniformLocation(shader.program, name);
            checkError();
            shader.setUniformLocation(name, location);
            checkError();
        }

        return location;
    }

    private int getType(VertexAttribPointer.Type type) {
        final int rv;
        if (type == VertexAttribPointer.Type.Float)
            rv = GLES30.GL_FLOAT;
        else
            throw new UnsupportedException("Unknown type: " + type);
        return rv;
    }

    private int getFactor(BlendFunc.Factor factor) {
        final int rv;
        if (factor == BlendFunc.Factor.SRC_ALPHA)
            rv = GLES30.GL_SRC_ALPHA;
        else if (factor == BlendFunc.Factor.ONE_MINUS_SRC_ALPHA)
            rv = GLES30.GL_ONE_MINUS_SRC_ALPHA;
        else
            throw new UnsupportedException("Unknown factor: " + factor);
        return rv;
    }

    private int getDepthFunc(DepthFunc.Function depthFunc) {
        final int rv;
        if (depthFunc == DepthFunc.Function.LESS)
            rv = GLES30.GL_LESS;
        else
            throw new UnsupportedException("Unknown depth function: " + depthFunc);
        return rv;
    }

    private int getTextureUnit(int value) {
        final int rv;
        if (value == 0)
            rv = GLES30.GL_TEXTURE0;
        else
            throw new UnsupportedException("Unknown texture unit: " + value);
        return rv;
    }

    private int getCapability(Enable.Capability capability) {
        final int rv;
        if (capability == Enable.Capability.DEPTH_TEST)
            rv = GLES30.GL_DEPTH_TEST;
        else if (capability == Enable.Capability.BLEND)
            rv = GLES30.GL_BLEND;
        else
            throw new UnsupportedException("Unknown capability: " + capability);
        return rv;
    }

    @Override
    public void run(final BufferData command) {
        GLES30.glBufferData(getTarget(command.getTarget()), command.getSize(), command.getData(), getUsage(command.getUsage()));
        checkError();
    }

    @Override
    public void run(final Clear command) {
        GLES30.glClear(getMask(command.getModes()));
        checkError();
    }

    @Override
    public void run(final CompileShader command) {
        int shader = GLES30.glCreateShader(getShaderType(command.getShaderType()));
        checkError();

        GLES30.glShaderSource(shader, command.getShaderSource("300 es"));
        checkError();

        GLES30.glCompileShader(shader);
        checkError();

        IntBuffer success = GLUtil.intBuffer(1);
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, success);
        checkError();
        if (success.get() == 0) {
            String info = GLES30.glGetShaderInfoLog(shader);
            checkError();
            throw new RendererException("Error compiling shader: " + info);
        }
        GLES30.glAttachShader(command.getProgram().program, shader);
        checkError();
    }

    @Override
    public void run(final CreateProgram command) {
        command.getProgram().program = GLES30.glCreateProgram();
        checkError();
    }

    @Override
    public void run(final DrawArrays command) {
        GLES30.glDrawArrays(getMode(command.getMode()), command.getFirst(), command.getCount());
        checkError();
    }

    @Override
    public void run(final DrawElements command) {
        GLES30.glDrawElements(getMode(command.getMode()), command.getCount(), GLES30.GL_UNSIGNED_INT, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void run(final GenerateArray command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        GLES30.glGenVertexArrays(1, tmp);
        checkError();
        command.getVao().vao = tmp.get();
    }

    @Override
    public void run(final GenerateBuffer command) {
        IntBuffer tmp = GLUtil.intBuffer(1);
        GLES30.glGenBuffers(1, tmp);
        checkError();
        command.getVbo().vbo = tmp.get();
    }

    @Override
    public void run(final GenerateFramebuffer command) {

    }

    public void checkError() {
        if (DEBUG) {
            int error;
            StringBuilder sb = new StringBuilder();
            while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
                sb.append(error);
            }

            String msg = sb.toString();
            if (msg.length() > 0) {
                throw new RuntimeException("glError: " + msg);
            }
        }
    }

    private Bitmap getBitmap(Buffer buffer, int width, int height) {
        buffer.rewind();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
    }

    @Override
    public void run(final GenerateTexture command) {
        IntBuffer buffer = GLUtil.intBuffer(1);
        GLES30.glGenTextures(1, buffer);
        checkError();
        command.getTexture().texture = buffer.get();
    }

    @Override
    public void run(final LinkProgram command) {
        GLES30.glLinkProgram(command.getProgram().program);
        checkError();
        IntBuffer success = GLUtil.intBuffer(1);
        GLES30.glGetProgramiv(command.getProgram().program, GLES30.GL_LINK_STATUS, success);
        checkError();
        if (success.get() == 0) {
            String info = GLES30.glGetProgramInfoLog(command.getProgram().program);
            checkError();
            throw new RendererException("Error linking shader: " + info);
        }
    }

    @Override
    public void run(final LoadTexture command) {
        Bitmap bitmap = getBitmap(command.getBuffer(), command.getWidth(), command.getHeight());
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        checkError();
        bitmap.recycle();
    }

    @Override
    public void run(final SetUniformMatrix command) {
        GLES30.glUniformMatrix4fv(getUniformLocation(command.getShader(), command.getAttribute()), command.getCount(), command.isTranspose(), command.getBuffer());
        checkError();
    }

    @Override
    public void run(final SetUniform command) {
        if (command.getType() == SetUniform.Type.INT) {
            GLES30.glUniform1i(getUniformLocation(command.getProgram(), command.getAttribute()), 0);
            checkError();
        }
    }

    @Override
    public void run(final VertexAttribPointer command) {
        GLES30.glVertexAttribPointer(command.getIndex(), command.getSize(), getType(command.getType()), command.isNormalized(), command.getStride(), command.getPointer());
        checkError();
    }

    @Override
    public void run(final EnableVertexAttribArray command) {
        GLES30.glEnableVertexAttribArray(command.getIndex());
        checkError();
    }

    @Override
    public void run(final ClearColor command) {
        GLES30.glClearColor(command.getR(), command.getG(), command.getB(), command.getA());
        checkError();
    }

    @Override
    public void run(final BlendFunc command) {
        GLES30.glBlendFunc(getFactor(command.getSfactor()), getFactor(command.getDfactor()));
        checkError();
    }

    @Override
    public void run(final DepthFunc command) {
        GLES30.glDepthFunc(getDepthFunc(command.getDepthFunc()));
        checkError();
    }

    @Override
    public void run(final TextureParameter command) {
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, getFilter(command.getMin()));
        checkError();
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, getFilter(command.getMag()));
        checkError();
    }

    @Override
    public void run(final GenerateMipMap command) {

    }

    @Override
    public void set(final ActivateTexture command) {
        GLES30.glActiveTexture(getTextureUnit(command.getTextureUnit()));
        checkError();
    }

    @Override
    public void clear(final ActivateTexture command) {

    }

    @Override
    public void set(final BindBuffer command) {
        GLES30.glBindBuffer(getTarget(command.getTarget()), command.getVBO().vbo);
        checkError();
    }

    @Override
    public void clear(final BindBuffer command) {
        GLES30.glBindBuffer(getTarget(command.getTarget()), 0);
        checkError();
    }

    @Override
    public void set(final BindFramebuffer command) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, command.getFramebuffer().fbo);
        checkError();
    }

    @Override
    public void clear(final BindFramebuffer command) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        checkError();
    }

    @Override
    public void set(final Enable command) {
        Enable.Capability cap = command.getCapability();
        if (cap == Enable.Capability.MULTISAMPLE || cap == Enable.Capability.POINT_SIZE)
            return;
        GLES30.glEnable(getCapability(cap));
        checkError();
    }

    @Override
    public void clear(final Enable command) {
        Enable.Capability cap = command.getCapability();
        if (cap == Enable.Capability.MULTISAMPLE || cap == Enable.Capability.POINT_SIZE)
            return;
        GLES30.glDisable(getCapability(cap));
        checkError();
    }

    @Override
    public void set(final UseProgram command) {
        GLES30.glUseProgram(command.getProgram().program);
        checkError();
    }

    @Override
    public void clear(final UseProgram command) {
        GLES30.glUseProgram(0);
        checkError();
    }

    @Override
    public void set(final BindTexture command) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, command.getTexture().texture);
        checkError();
    }

    @Override
    public void clear(final BindTexture command) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        checkError();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void set(final BindArray command) {
        GLES30.glBindVertexArray(command.getVAO().vao);
        checkError();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void clear(final BindArray command) {
        GLES30.glBindVertexArray(0);
        checkError();
    }
}
