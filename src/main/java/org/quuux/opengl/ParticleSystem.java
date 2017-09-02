package org.quuux.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.quuux.opengl.util.RandomUtil;
import org.quuux.opengl.util.ResourceUtil;


public class ParticleSystem implements Entity {

    private static final int NUM_PARATICLES = 5000;

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    Vector3d position;
    List<Particle> particles = new ArrayList<>(NUM_PARATICLES);

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(6 * NUM_PARATICLES);
    int vertexBufferId;

    Texture texture;
    ShaderProgram shader;

    FrameBuffer framebuffer;
    Texture renderTexture;

    ParticleSystem(Vector3d position, GL4 gl) {
        this.position = position;
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = new Particle();
            particles.add(p);
            recycleParticle(p);
        }

//        framebuffer = new FrameBuffer(gl);
//        framebuffer.bind(gl);
//
//        renderTexture = new Texture(gl);
//        renderTexture.attach(gl, Sandbox.WIDTH, Sandbox.HEIGHT, GL.GL_RGB, null);

        gl.glPointSize(16);

        shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource("shaders/particle.vert.glsl"));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource("shaders/particle.frag.glsl"));
        shader.link(gl);
        gl.glUseProgram(shader.program);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture = new Texture(gl);
        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/particle.png");
        texture.attach(gl, image.width, image.height,  GL.GL_RGBA, image.buffer);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        gl.glUniform1i(gl.glGetUniformLocation(shader.program, "particleTexture"), 0);

        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, vertexBufferId);
        this.vertexBufferId = vertexBufferId.get(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vertexBufferId);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

    }

    private void recycleParticle(Particle p) {
        Vector3d acceleration = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
        acceleration.mul(.0001);
        Vector3d velocity = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
        velocity.mul(.02f);

        Vector3d color = new Vector3d(RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1));
        p.recycle(new Vector3d(position), velocity, acceleration, color);
    }

    @Override
    public void update(long t) {
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = particles.get(i);
            p.update(t);
            if (!p.isAlive())
                recycleParticle(p);
        }
    }

    @Override
    public void dispose(GL4 gl) {
        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        vertexBufferId.put(this.vertexBufferId);
        gl.glDeleteBuffers(1, vertexBufferId);
    }

    @Override
    public void draw(GL4 gl, Camera camera) {
        updateVertices(camera);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        camera.modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(shader.program, "mvp"), 1, false, mvpBuffer);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);
        gl.glDrawArrays(GL.GL_POINTS, 0, NUM_PARATICLES);
    }

    private void updateVertices(Camera camera) {

        for (int i=0; i<NUM_PARATICLES; i++) {

            Particle p = particles.get(i);
            int offset = 6 * i;
            vertexBuffer.put(offset, (float) p.position.x);
            vertexBuffer.put(offset + 1, (float) p.position.y);
            vertexBuffer.put(offset + 2, (float) p.position.z);
            vertexBuffer.put(offset + 3, (float) p.color.x);
            vertexBuffer.put(offset + 4, (float) p.color.y);
            vertexBuffer.put(offset + 5, (float) p.color.z);
        }

        Collections.sort(particles, particleComparator);
    }

    static class Particle {
        int age = 0;
        int lifespan = 500;
        Vector3d position;
        Vector3d velocity;
        Vector3d acceleration;
        Vector3d color;

        boolean isAlive() {
            return age < lifespan;
        }

        void recycle(Vector3d position, Vector3d velocity, Vector3d acceleration, Vector3d color) {
            age = 0;
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.color = color;
        }

        void update(long t) {
            age += 1;
            if (isAlive()) {
                this.velocity.add(this.acceleration);
                this.position.add(this.velocity);
            }
        }
    }

    Comparator<Particle> particleComparator = new Comparator<Particle>() {
        @Override
        public int compare(Particle o1, Particle o2) {
            if (o1.position.z == o2.position.z)
                return 0;
            return (o1.position.z < o2.position.z) ? -1 : 1;
        }
    };
}
