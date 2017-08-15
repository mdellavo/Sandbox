package org.quuux.opengl;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ShaderProgram {

    public int program = 0;
    List<Integer> shaders = new ArrayList<>();

    public ShaderProgram(GL4 gl) {
        program = gl.glCreateProgram();
    }

    public String byteBufferToString(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }

    public int compileShader(GL4 gl, int shaderType, String shaderSource) {
        int shader = gl.glCreateShader(shaderType);
        gl.glShaderSource(shader, 1, new String[] {shaderSource}, null);
        gl.glCompileShader(shader);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl.glGetShaderInfoLog(shader, buffer.capacity(), null, buffer);
            System.out.println("Error compiling shader: " + byteBufferToString(buffer));
            return -1;
        }

        return shader;
    }

    public int addShader(GL4 gl, int shaderType, String shaderSource) {
        int shader = compileShader(gl, shaderType, shaderSource);
        shaders.add(shader);
        gl.glAttachShader(program, shader);
        return shader;
    }

    public int link(GL4 gl) {
        gl.glLinkProgram(program);

        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(program, GL4.GL_LINK_STATUS, success);
        if (success.get(0) == 0) {
            ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
            gl.glGetProgramInfoLog(program, buffer.capacity(), null, buffer);

            System.out.println("Error linking shader: " + byteBufferToString(buffer));
            return -1;
        }
        return program;
    }

}
