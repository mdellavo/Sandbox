package org.quuux.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Quad implements Entity {

    final float[] vertices = new float[] {
            -.5f, -.5f, 0,
            .5f, -.5f, 0,
            .5f, .5f, 0,
            -.5f, -.5f, 0,
            -.5f, .5f, 0,
            .5f, .5f, 0,
    };

    final float[] textureCoords = new float[] {

    };

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertices.length);
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

        gl.glUniform1i(gl.glGetUniformLocation(shader.program, "texture"), 0);

        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, vertexBufferId);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferId.get(0));

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);
    }

    @Override
    public void dispose(GL4 gl) {

    }

    @Override
    public void draw(GL4 gl, Camera camera) {
        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        camera.modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(shader.program, "mvp"), 1, false, mvpBuffer);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);

    }

    @Override
    public void update(long t) {

    }
}
