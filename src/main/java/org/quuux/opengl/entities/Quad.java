package org.quuux.opengl.entities;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.scenes.Scene;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Quad implements Entity {

    float vertices[] = {
            // verticies         // texture coords
             0.5f,  0.5f, 0.0f,  1.0f, 1.0f,  // top right
             0.5f, -0.5f, 0.0f,  1.0f, 0.0f,  // bottom right
            -0.5f,  0.5f, 0.0f,  0.0f, 1.0f,  // top left

             0.5f, -0.5f, 0.0f,  1.0f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  0.0f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f,  0.0f, 1.0f,  // top left
    };

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    int vbo;
    int vao;

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertices);
    Texture texture;
    ShaderProgram shader;

    public Quad(Vector3d position, GL4 gl) {

        model.translate(position);

        shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource("shaders/quad.vert.glsl"));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource("shaders/quad.frag.glsl"));
        shader.link(gl);
        gl.glUseProgram(shader.program);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture = new Texture(gl);
        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/test-pattern.png");
        texture.attach(gl, image.width, image.height,  GL.GL_RGBA, image.buffer);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        gl.glUniform1i(shader.getUniformLocation(gl, "texture"), 0);

        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);

        gl.glGenVertexArrays(1, tmp);
        this.vao = tmp.get(0);

        gl.glBindVertexArray(this.vao);

        gl.glGenBuffers(1, tmp);
        this.vbo = tmp.get(0);

        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 2, GL.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);
    }

    @Override
    public void dispose(GL4 gl) {

    }

    @Override
    public void draw(GL4 gl) {
        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        gl.glBindVertexArray(this.vao);

        Scene.getScene().getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(shader.getUniformLocation(gl, "mvp"), 1, false, mvpBuffer);

        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
    }

    @Override
    public void update(long t) {

    }
}
