package org.quuux.opengl.lib;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.quuux.opengl.renderer.Bindable;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShaderProgram implements Bindable {

    public int program = 0;
    List<Integer> shaders = new ArrayList<>();

    Map<String, Integer> uniformCache = new HashMap<>();

    public ShaderProgram(GL gl) {
        program = gl.getGL4().glCreateProgram();
    }

    public void clear(GL gl) {
        gl.getGL4().glUseProgram(0);
    }

    public String byteBufferToString(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }

    public int compileShader(GL gl, int shaderType, String shaderSource) {

        GL4 gl4 = gl.getGL4();

        int shader = gl4.glCreateShader(shaderType);
        gl4.glShaderSource(shader, 1, new String[] {shaderSource}, null);
        gl4.glCompileShader(shader);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl4.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl4.glGetShaderInfoLog(shader, buffer.capacity(), null, buffer);
            System.out.println("Error compiling shader: " + byteBufferToString(buffer));
            return -1;
        }

        return shader;
    }

    public int addShader(GL gl, int shaderType, String shaderSource) {
        int shader = compileShader(gl, shaderType, shaderSource);
        shaders.add(shader);
        gl.getGL4().glAttachShader(program, shader);
        return shader;
    }

    public int link(GL gl) {
        GL4 gl4 = gl.getGL4();
        gl4.glLinkProgram(program);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl4.glGetProgramiv(program, GL4.GL_LINK_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl4.glGetProgramInfoLog(program, buffer.capacity(), null, buffer);

            System.out.println("Error linking shader: " + byteBufferToString(buffer));
            return -1;
        }
        return program;
    }

    public int getUniformLocation(GL gl, String name) {
        Integer location = uniformCache.get(name);
        if (location == null) {
            location = gl.getGL4().glGetUniformLocation(program, name);
            uniformCache.put(name, location);

        }
        return location;
    }

    public void use(GL gl) {
        gl.getGL4().glUseProgram(program);
    }

    public void bind(GL gl) {
        use(gl);
    }

    public static ShaderProgram build(GL gl, String vertrexShaderPath, String fragmentShaderPath) {
        ShaderProgram shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource(vertrexShaderPath));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource(fragmentShaderPath));
        shader.link(gl);
        shader.use(gl);
        return shader;
    }
}
