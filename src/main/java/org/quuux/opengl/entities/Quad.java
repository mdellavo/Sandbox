package org.quuux.opengl.entities;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.scenes.Scene;
import org.quuux.opengl.util.Log;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Quad implements Entity {

    float vertices[] = {
            // verticies         // texture coords
             1,  1, 0,  1, 1,  // top right
             1, -1, 0,  1, 0,  // bottom right
            -1,  1, 0,  0, 1,  // top left

             1, -1, 0,  1, 0,  // bottom right
            -1, -1, 0,  0, 0,  // bottom left
            -1,  1, 0,  0, 1,  // top left
    };

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    VBO vbo;
    VAO vao;

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertices);
    Texture texture;
    ShaderProgram shader;

    public Quad(GL4 gl) {
        //Log.out("*** quad init");

        shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource("shaders/quad.vert.glsl"));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource("shaders/quad.frag.glsl"));
        shader.link(gl);
        shader.bind(gl);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        gl.glUniform1i(shader.getUniformLocation(gl, "texture"), 0);

        vao = new VAO(gl);
        vbo = new VBO(gl);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 2, GL.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

        vao.clear(gl);
        vbo.clear(gl);
        shader.clear(gl);
    }

    @Override
    public void dispose(GL4 gl) {

    }

    @Override
    public void draw(GL4 gl) {
        //Log.out("*** quad draw");

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        shader.bind(gl);

        vbo.bind(gl);
        vao.bind(gl);

        Scene.getScene().getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(shader.getUniformLocation(gl, "mvp"), 1, false, mvpBuffer);

        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);

        vao.clear(gl);
        texture.clear(gl);
        vbo.clear(gl);
        shader.clear(gl);
    }

    @Override
    public void update(long t) {
        // noop
    }

    public Matrix4d getModel() {
        return model;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
