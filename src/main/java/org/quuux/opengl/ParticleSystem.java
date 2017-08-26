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
import org.quuux.opengl.util.RandomUtil;
import org.quuux.opengl.util.ResourceUtil;


public class ParticleSystem implements Entity {

    private static final int NUM_PARATICLES = 500;

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    Vec3 position;
    List<Particle> particles = new ArrayList<>(NUM_PARATICLES);

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(6 * NUM_PARATICLES);
    IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);

    Texture texture;
    ShaderProgram shader;

    ParticleSystem(Vec3 position) {
        this.position = position;
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = new Particle();
            particles.add(p);
            recycleParticle(p);
        }
    }

    private void recycleParticle(Particle p) {
        Vec3 acceleration = new Vec3(0, -.0002f, 0);

        Vec3 velocity = new Vec3(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
        velocity.scale(.02f);

        Vec3 color = new Vec3(RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1));
        p.recycle(new Vec3(position), velocity, acceleration, color);
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

        gl.glPointSize(10);

        shader = new ShaderProgram(gl);
        shader.addShader(gl, GL4.GL_VERTEX_SHADER, ResourceUtil.getStringResource("shaders/particle.vert.glsl"));
        shader.addShader(gl, GL4.GL_FRAGMENT_SHADER, ResourceUtil.getStringResource("shaders/particle.frag.glsl"));
        shader.link(gl);
        gl.glUseProgram(shader.program);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture = new Texture(gl);
        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/particle.png");
        texture.load(gl, GL4.GL_LINEAR, GL4.GL_LINEAR, image.width, image.height, image.buffer);

        gl.glUniform1i(gl.glGetUniformLocation(shader.program, "particleTexture"), 0);

        gl.glGenBuffers(1, vertexBufferId);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferId.get(0));

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

    }

    @Override
    public void dispose(GL4 gl) {
        gl.glDeleteBuffers(1, vertexBufferId);
    }

    @Override
    public void draw(GL4 gl, Camera camera) {
        updateVertices();

        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        camera.modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(shader.program, "mvp"), 1, false, mvpBuffer);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);
        gl.glDrawArrays(GL.GL_POINTS, 0, NUM_PARATICLES);
    }

    private void updateVertices() {
        Collections.sort(particles, comparator);
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
    }

    static class Particle {
        int age = 0;
        int lifespan = 100;
        Vec3 position;
        Vec3 velocity;
        Vec3 acceleration;
        Vec3 color;

        boolean isAlive() {
            return age < lifespan;
        }

        void recycle(Vec3 position, Vec3 velocity, Vec3 acceleration, Vec3 color) {
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

    static Comparator<Particle> comparator = new Comparator<Particle>() {
        @Override
        public int compare(Particle a, Particle b) {
            return -Double.compare(a.position.z, b.position.z);
        }
    };
}
