package org.quuux.opengl.entities;

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
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.scenes.Scene;
import org.quuux.opengl.util.Log;
import org.quuux.opengl.util.RandomUtil;
import org.quuux.opengl.util.ResourceUtil;


public class ParticleEmitter implements Entity {

    private static final int NUM_PARATICLES = 500;
    private static final int TOTAL_PARTICLES = NUM_PARATICLES * 10;
    private static final int PARTICLE_SIZE = 24;
    private static final int PARTICLE_LIFESPAN = 100;

    long ticks;

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    Vector3d position = new Vector3d();

    List<Particle> particles = new ArrayList<>();
    List<Particle> pool = new ArrayList<>();

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(8 * TOTAL_PARTICLES);

    int vbo;
    int vao;

    Texture texture;
    ShaderProgram shader;

    public ParticleEmitter(GL4 gl) {
        //Log.out("*** emitter init");

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

        gl.glUniform1i(shader.getUniformLocation(gl, "particleTexture"), 0);

        IntBuffer tmp = GLBuffers.newDirectIntBuffer(1);
        gl.glGenVertexArrays(1, tmp);
        vao = tmp.get(0);

        gl.glBindVertexArray(vao);

        gl.glGenBuffers(1, tmp);
        vbo = tmp.get(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 8 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

        gl.glVertexAttribPointer(2, 1, GL.GL_FLOAT, false, 8 * Float.BYTES, 7 * Float.BYTES);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(0);
        texture.clear(gl);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }

    private Particle allocateParticle() {
        if (particles.size() >= TOTAL_PARTICLES)
            return null;
        Particle p = (pool.size() > 0) ? pool.remove(pool.size() - 1) : new Particle();
        particles.add(p);
        return p;
    }

    private void recycleParticle(Particle p) {
        particles.remove(p);
        pool.add(p);
    }

    private void seedParticles() {
        for (int i=0; i<NUM_PARATICLES; i++) {
            Particle p = allocateParticle();
            p.hasTrail = true;
            Vector3d position = new Vector3d(this.position);

            Vector3d acceleration = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
            acceleration.mul(.0001);

            Vector3d velocity = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
            velocity.mul(.02f);

            Vector3d color = new Vector3d(RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1), RandomUtil.randomRange(.5f, 1));
            int lifespan = RandomUtil.randomInt(PARTICLE_LIFESPAN / 2, PARTICLE_LIFESPAN * 2);
            p.recycle(position, velocity, acceleration, color, lifespan);
        }
    }

    private void trail(Particle p) {
        Particle t = allocateParticle();
        if (t == null)
            return;
        t.hasTrail = false;
        Vector3d position = new Vector3d(p.position);
        Vector3d velocity = new Vector3d();
        velocity.add(
                RandomUtil.randomRange(.0001, .01),
                RandomUtil.randomRange(.0001, .01),
                RandomUtil.randomRange(.0001, .01)
                );
        Vector3d acceleration = new Vector3d();
        t.recycle(position, velocity, acceleration, p.color, p.lifespan / 5);
    }

    @Override
    public void update(long t) {
        for (int i=0; i<particles.size(); i++) {
            Particle p = particles.get(i);
            p.update(t);
            if (!p.isAlive()) {
                recycleParticle(p);
            } else if (p.hasTrail) {
                trail(p);
            }
        }

        if (particles.size() == 0) {
            seedParticles();
        }

        ticks++;
    }

    @Override
    public void dispose(GL4 gl) {
        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        vertexBufferId.put(this.vbo);
        gl.glDeleteBuffers(1, vertexBufferId);
    }

    @Override
    public void draw(GL4 gl) {
        //Log.out("*** emitter draw");

        updateVertices(vertexBuffer);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.bind(gl);

        gl.glUseProgram(shader.program);

        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo);
        gl.glBindVertexArray(vao);

        Scene.getScene().getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
        gl.glUniformMatrix4fv(shader.getUniformLocation(gl, "mvp"), 1, false, mvpBuffer);

        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW);
        gl.glDrawArrays(GL.GL_POINTS, 0, particles.size());

        gl.glBindVertexArray(0);
        texture.clear(gl);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
        gl.glUseProgram(0);

    }

    private void updateVertices(FloatBuffer vertexBuffer) {
        Collections.sort(particles, particleComparator);

        for (int i=0; i<particles.size(); i++) {
            Particle p = particles.get(i);
            int offset = 8 * i;
            float agePercentile = (float) ((double) p.age / (double) p.lifespan);
            vertexBuffer.put(offset, (float) p.position.x);
            vertexBuffer.put(offset + 1, (float) p.position.y);
            vertexBuffer.put(offset + 2, (float) p.position.z);
            vertexBuffer.put(offset + 3, (float) p.color.x);
            vertexBuffer.put(offset + 4, (float) p.color.y);
            vertexBuffer.put(offset + 5, (float) p.color.z);
            vertexBuffer.put(offset + 6, .75f * (1 - agePercentile));
            vertexBuffer.put(offset + 7, PARTICLE_SIZE * (1 - agePercentile));
        }
    }

    static class Particle {
        int age = 0;
        int lifespan;
        Vector3d position;
        Vector3d velocity;
        Vector3d acceleration;
        Vector3d color;
        boolean hasTrail;

        boolean isAlive() {
            return age < lifespan;
        }

        void recycle(Vector3d position, Vector3d velocity, Vector3d acceleration, Vector3d color, int lifespan) {
            age = 0;
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.color = color;
            this.lifespan = lifespan;
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
            Camera camera = Scene.getScene().getCamera();
            double d1 = camera.eye.distance(o1.position);
            double d2 = camera.eye.distance(o2.position);
            return -Double.compare(d1, d2);
        }
    };
}
