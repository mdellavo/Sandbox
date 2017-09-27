package org.quuux.opengl.entities;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.BatchState;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BindVertex;
import org.quuux.opengl.renderer.states.SetUniformMatrix;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.scenes.Camera;

import java.nio.FloatBuffer;

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
    Texture2D texture;
    ShaderProgram shader;

    public Quad(GL4 gl) {
        //Log.out("*** quad init");

        shader = ShaderProgram.build(gl, "shaders/quad.vert.glsl", "shaders/quad.frag.glsl");

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
    public void dispose(GL gl) {

    }

    @Override
    public Command draw() {
        BatchState rv = new BatchState(new ActivateTexture(GL.GL_TEXTURE0, texture), new UseProgram(shader), new BindVertex(vbo, vao));

        Camera.getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);

        rv.add(new SetUniformMatrix(shader, "mvp", 1, false, mvpBuffer));
        rv.add(new DrawArrays(GL.GL_TRIANGLES, 0, 6));
        return rv;
    }

    @Override
    public void update(long t) {
        // noop
    }

    public Matrix4d getModel() {
        return model;
    }

    public void setTexture(Texture2D texture) {
        this.texture = texture;
    }

}
