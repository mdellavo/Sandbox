package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import org.quuux.opengl.Config;
import org.quuux.opengl.entities.ParticleEmitter;
import org.quuux.opengl.entities.Quad;
import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.lib.Texture2D;


public class TestScene extends Scene {

    long ticks, totalElapsed;

    FrameBuffer frameBuffer;
    ParticleEmitter pe;
    Quad quad;

    @Override
    public void setup(GL gl) {
        //Log.out("*** setup scene");

        super.setup(gl);

        GL4 gl4 = gl.getGL4();

        gl4.glClearColor(0, 0, 0, 1);
        gl4.glEnable(GL4.GL_BLEND);
        gl4.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);

        gl4.glEnable(GL4.GL_DEPTH_TEST);
        gl4.glDepthFunc(GL4.GL_LESS);
        gl4.glEnable(GL4.GL_MULTISAMPLE);
        gl4.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

        frameBuffer = new FrameBuffer(gl4, Config.WIDTH, Config.HEIGHT);

        Texture2D texture = new Texture2D(gl4);
        texture.bind(gl4);
        texture.attach(gl4, GL4.GL_RGBA16F, Config.WIDTH, Config.HEIGHT, GL4.GL_RGBA, null);
        texture.setFilterParameters(gl4, GL.GL_LINEAR, GL.GL_LINEAR);
        texture.clear(gl4);

        frameBuffer.attach(gl4, texture);
        frameBuffer.clear(gl4);

        pe = new ParticleEmitter(gl4);

        quad = new Quad(gl4);
        quad.setTexture(texture);

        Camera.getCamera().setEye(0, 5, 5);
    }

    @Override
    public void update(long t) {
        ticks += 1;
        totalElapsed += t;

        double angle = (totalElapsed % 5000) / 5000. * 360.;
        double eyeX = 5 * Math.cos(Math.toRadians(angle));
        double eyeZ = 5 * Math.sin(Math.toRadians(angle));
        Camera.getCamera().setEye(eyeX, 5, eyeZ);

        pe.update(t);
    }

    @Override
    public void draw(GL gl) {
        //Log.out("*** draw scene");

        // pass 1 - render particles
        frameBuffer.bind(gl);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        pe.draw(gl);
        frameBuffer.clear(gl);

        // render texture
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        quad.draw(gl);
    }
}
