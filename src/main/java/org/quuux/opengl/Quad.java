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
            0, 0,
            1, 0,
            1, 1,
            0, 0,
            0, 1,
            1, 1,
    };

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertices.length + textureCoords.length);
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

        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, vertexBufferId);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferId.get(0));

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

        updateBufferData();
    }

    private void updateBufferData() {
        int rows = vertices.length / 3;
        for (int i=0; i<rows; i++) {
            int offset = i * 5;
            vertexBuffer.put(offset, vertices[i]);
            vertexBuffer.put(offset+1, vertices[i+1]);
            vertexBuffer.put(offset+2, vertices[i+2]);
            vertexBuffer.put(offset+3, textureCoords[i]);
            vertexBuffer.put(offset+4, textureCoords[i+1]);
        }
    }

    @Override
    public void dispose(GL4 gl) {

    }

    @Override
    public void draw(GL4 gl) {
        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        Scene.getScene().getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(shader.getUniformLocation(gl, "mvp"), 1, false, mvpBuffer);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);

    }

    @Override
    public void update(long t) {

    }
}
