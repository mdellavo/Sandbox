package org.quuux.opengl.entities;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.VBO;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.util.GLUtil;
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
    FloatBuffer mvpBuffer = GLUtil.floatBuffer(16);

    Vector3d position = new Vector3d();

    List<Particle> particles = new ArrayList<>();
    List<Particle> pool = new ArrayList<>();

    FloatBuffer vertexBuffer = GLUtil.floatBuffer(8 * TOTAL_PARTICLES);

    VBO vbo = new VBO();
    VAO vao = new VAO();

    Texture2D texture = new Texture2D();
    ShaderProgram shader = new ShaderProgram();

    Command displayList;

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
    public Command dispose() {
        return null;
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateTexture2D(texture));

        rv.add(ShaderProgram.build(shader, ResourceUtil.getStringResource("shaders/particle.vert.glsl"), ResourceUtil.getStringResource("shaders/particle.frag.glsl")));

        BatchState ctx = new BatchState(new UseProgram(shader), new BindBuffer(vbo), new BindArray(vao), new BindTexture(texture), new ActivateTexture(0));
        rv.add(ctx);

        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/particle1.png");
        ctx.add(new LoadTexture2D(texture, LoadTexture2D.Format.SRGB_ALPHA, image.width, image.height,  LoadTexture2D.Format.RGBA, image.buffer, LoadTexture2D.Filter.LINEAR, LoadTexture2D.Filter.LINEAR));

        ctx.add(new SetUniform(shader, "texture", SetUniform.Type.INT, 0));

        ctx.add(new VertexAttribPointer(0, 3, VertexAttribPointer.Type.Float, false, 8 * 4, 0));
        ctx.add(new EnableVertexAttribArray(0));

        ctx.add(new VertexAttribPointer(1, 4, VertexAttribPointer.Type.Float, false, 8 * 4, 3 * 4));
        ctx.add(new EnableVertexAttribArray(1));

        ctx.add(new VertexAttribPointer(2, 1, VertexAttribPointer.Type.Float, false, 8 * 4, 7 * 4));
        ctx.add(new EnableVertexAttribArray(2));

        return rv;
    }

    @Override
    public Command draw() {
        updateVertices(vertexBuffer);
        Camera.getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);

        if (displayList == null) {
            BatchState rv = new BatchState(new UseProgram(shader), new BindArray(vao), new BindBuffer(vbo), new BindTexture(texture), new ActivateTexture(0));
            rv.add(new SetUniformMatrix(shader, "mvp", 1, false, mvpBuffer));
            rv.add(new BufferData(BufferData.Target.ArrayBuffer, vertexBuffer.capacity() * 4, vertexBuffer, BufferData.Usage.StreamDraw));
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
            super(Mode.Points, 0, particles.size());
        }

        @Override
        public int getCount() {
            return particles.size();
        }
    }
}
