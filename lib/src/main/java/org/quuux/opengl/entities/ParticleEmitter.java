package org.quuux.opengl.entities;

import java.awt.*;
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
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.BatchState;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BindVertex;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.util.RandomUtil;
import org.quuux.opengl.util.ResourceUtil;


public class ParticleEmitter implements Entity {

    private static final int NUM_PARATICLES = 1000;
    private static final int TOTAL_PARTICLES = NUM_PARATICLES * 10;
    private static final int PARTICLE_SIZE = 64;
    private static final int PARTICLE_LIFESPAN = 75;

    long ticks;

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLBuffers.newDirectFloatBuffer(16);

    Vector3d position = new Vector3d();

    List<Particle> particles = new ArrayList<>();
    List<Particle> pool = new ArrayList<>();

    FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(8 * TOTAL_PARTICLES);

    VBO vbo;
    VAO vao;

    Texture2D texture;
    ShaderProgram shader;

    Command displayList;

    public ParticleEmitter(GL gl) {
        //Log.out("*** emitter init");

        GL4 gl4 = gl.getGL4();
        shader = ShaderProgram.build(gl4, "shaders/particle.vert.glsl", "shaders/particle.frag.glsl");

        gl4.glActiveTexture(GL.GL_TEXTURE0);
        texture = new Texture2D(gl4);
        texture.bind(gl4);
        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/particle1.png");

        texture.attach(gl4, GL4.GL_SRGB_ALPHA, image.width, image.height,  GL4.GL_RGBA, image.buffer);
        texture.setFilterParameters(gl4, GL.GL_LINEAR, GL.GL_LINEAR);

        gl4.glUniform1i(shader.getUniformLocation(gl4, "texture"), 0);

        vao = new VAO(gl4);
        vbo = new VBO(gl4);

        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 8 * Float.BYTES, 0);
        gl4.glEnableVertexAttribArray(0);

        gl4.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        gl4.glEnableVertexAttribArray(1);

        gl4.glVertexAttribPointer(2, 1, GL.GL_FLOAT, false, 8 * Float.BYTES, 7 * Float.BYTES);
        gl4.glEnableVertexAttribArray(2);

        vao.clear(gl4);
        texture.clear(gl4);
        vbo.clear(gl4);
        shader.clear(gl4);
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
            p.emitsTrail = true;
            Vector3d position = new Vector3d(this.position);

            Vector3d acceleration = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
            acceleration.mul(.0001);

            Vector3d velocity = new Vector3d(RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1), RandomUtil.randomRange(-1, 1));
            velocity.mul(.02f);

            Vector3d color = new Vector3d(RandomUtil.randomRange(0, 1), 1, 1);

            int lifespan = RandomUtil.randomInt(PARTICLE_LIFESPAN / 2, PARTICLE_LIFESPAN * 2);
            p.recycle(position, velocity, acceleration, color, lifespan);
        }
    }

    private void trail(Particle p) {
        Particle t = allocateParticle();
        if (t == null)
            return;
        t.emitsTrail = false;
        Vector3d position = new Vector3d(p.position);
        Vector3d velocity = new Vector3d();
        velocity.add(
                RandomUtil.randomRange(.0001, .01),
                RandomUtil.randomRange(.0001, .01),
                RandomUtil.randomRange(.0001, .01)
                );
        Vector3d acceleration = new Vector3d();
        t.recycle(position, velocity, acceleration, p.color, p.lifespan / 4);
    }

    @Override
    public void update(long t) {
        for (int i=0; i<particles.size(); i++) {
            Particle p = particles.get(i);
            p.update(t);
            if (!p.isAlive()) {
                recycleParticle(p);
            } else if (p.emitsTrail) {
                trail(p);
            }
        }

        if (particles.size() == 0) {
            seedParticles();
        }

        ticks++;
    }

    @Override
    public void dispose(GL gl) {
        IntBuffer vertexBufferId = GLBuffers.newDirectIntBuffer(1);
        vertexBufferId.put(this.vbo.vbo);
        gl.glDeleteBuffers(1, vertexBufferId);
    }

    @Override
    public Command draw() {
        updateVertices(vertexBuffer);
        Camera.getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);

        if (displayList == null) {
            BatchState rv = new BatchState(new ActivateTexture(GL.GL_TEXTURE0, texture), new UseProgram(shader), new BindVertex(vbo, vao));
            rv.add(new SetUniformMatrix(shader, "mvp", 1, false, mvpBuffer));
            rv.add(new BufferData(GL.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STREAM_DRAW));
            rv.add(new DrawParticles());
            displayList = rv;
        }

        return displayList;
    }

    private float colorComponent(int rgb, int shift) {
        int value = (rgb >> shift) & 0xFF;
        return (float)((double)value/255.);
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

            double hue = p.color.x + agePercentile;
            if (hue > 1)
                hue -= 1;

            int rgb = Color.HSBtoRGB(
                    (float)hue,
                    (float)1 - agePercentile,
                    (float)1 - agePercentile
            );

            vertexBuffer.put(offset + 3, colorComponent(rgb, 16));
            vertexBuffer.put(offset + 4, colorComponent(rgb, 8));
            vertexBuffer.put(offset + 5, colorComponent(rgb, 0));
            vertexBuffer.put(offset + 6,  (1 - agePercentile));

            double distance = Camera.getCamera().center.distance(p.position);
            double size = (PARTICLE_SIZE/distance) * agePercentile;
            if (size > PARTICLE_SIZE)
                size = PARTICLE_SIZE;
            vertexBuffer.put(offset + 7, (float) size);
        }
    }

    static class Particle {
        int age = 0;
        int lifespan;
        Vector3d position;
        Vector3d velocity;
        Vector3d acceleration;
        Vector3d color;
        boolean emitsTrail;

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
            Camera camera = Camera.getCamera();
            double d1 = camera.eye.distance(o1.position);
            double d2 = camera.eye.distance(o2.position);
            return -Double.compare(d1, d2);
        }
    };

    class DrawParticles extends DrawArrays {

        public DrawParticles() {
            super(0, 0, 0);
        }

        @Override
        public void run(GL gl) {
            gl.glDrawArrays(GL.GL_POINTS, 0, particles.size());
        }
    }
}
