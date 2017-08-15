package org.quuux.opengl;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

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

    public int checkSuccess(GL4 gl, int shader, int param) {
        IntBuffer success = GLBuffers.newDirectIntBuffer(1);
        gl.glGetShaderiv(shader, param, success);
        return success.get(0);
    }

    public String getProgramLog(GL4 gl, int shader) {
        ByteBuffer buffer = GLBuffers.newDirectByteBuffer(1024);
        gl.glGetProgramInfoLog(shader, buffer.capacity(), null, buffer);
        return buffer.toString();
    }

    public int compileShader(GL4 gl, int shaderType, String shaderSource) {
        int shader = gl.glCreateShader(shaderType);
        gl.glShaderSource(shader, 1, new String[] {shaderSource}, null);
        gl.glCompileShader(shader);

        if (checkSuccess(gl, shader, GL4.GL_COMPILE_STATUS) == 0) {
            System.out.println("Error compiling shader: " + getProgramLog(gl, shader));
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
        if (checkSuccess(gl, program, GL4.GL_LINK_STATUS) == 0) {
            System.out.println("Error linking shader: " + getProgramLog(gl, program));
            return -1;
        }
        return program;
    }

}
