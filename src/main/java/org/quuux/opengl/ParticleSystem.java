package org.quuux.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import com.jogamp.opengl.util.GLBuffers;
import org.quuux.opengl.util.RandomUtil;
import org.quuux.opengl.util.ResourceUtil;


public class ParticleSystem implements Entity {

    private static final int NUM_PARATICLES = 10;

    Vec3F position;
    List<Particle> particles = new ArrayList<>(NUM_PARATICLES);

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(6 * NUM_PARATICLES);
    IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);

    ShaderProgram shader;

    ParticleSystem(Vec3F position) {
        this.position = position;
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = new Particle();
            particles.add(p);
            recycleParticle(p);
        }
    }

    private void recycleParticle(Particle p) {
        Random random = RandomUtil.getInstance();
        Vec3F velocity = new Vec3F(random.nextFloat(), random.nextFloat(), random.nextFloat());
        Vec3F acceleration = new Vec3F(random.nextFloat(), random.nextFloat(), random.nextFloat());
        p.recycle(position, velocity, acceleration);
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
    public void initialize(GL4 gl) {
        gl.glGenBuffers(1, vertexBufferId);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferId.get(0));
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);

        shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource("shaders/particle.vert.glsl"));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource("shaders/particle.frag.glsl"));
        shader.link(gl);
    }

    @Override
    public void dispose(GL4 gl) {
        gl.glDeleteBuffers(1, vertexBufferId);
    }

    @Override
    public void draw(GL4 gl) {
        updateVertices();
        gl.glUseProgram(shader.program);
        gl.glDrawArrays(GL.GL_POINTS, 0, NUM_PARATICLES);
    }

    private void updateVertices() {
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = particles.get(i);
            int offset = 6 * i;
            vertexBuffer.put(offset, p.position.x);
            vertexBuffer.put(offset + 1, p.position.y);
            vertexBuffer.put(offset + 2, p.position.z);
            vertexBuffer.put(offset + 3,1);
            vertexBuffer.put(offset + 4, 1);
            vertexBuffer.put(offset + 5, 1);
        }
    }

    static class Particle {
        int age = 0;
        int lifespan = 100;
        Vec3F position;
        Vec3F velocity;
        Vec3F acceleration;
        Vec3F color;

        boolean isAlive() {
            return age < lifespan;
        }

        void recycle(Vec3F position, Vec3F velocity, Vec3F acceleration) {
            age = 0;
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        void update(long t) {
            age += 1;
            if (isAlive()) {
                this.velocity.add(this.acceleration);
                this.position.add(this.velocity);
            }
        }
    }
}
