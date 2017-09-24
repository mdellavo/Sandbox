package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import org.quuux.opengl.Config;
import org.quuux.opengl.entities.ParticleEmitter;
import org.quuux.opengl.entities.Quad;
import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.util.Log;


public class TestScene extends Scene {

    long ticks, totalElapsed;

    FrameBuffer frameBuffer;
    ParticleEmitter pe;
    Quad quad;

    @Override
    public void setup(GL4 gl) {
        //Log.out("*** setup scene");

        super.setup(gl);

        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL4.GL_BLEND);
        gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthFunc(GL4.GL_LESS);
        gl.glEnable(GL4.GL_MULTISAMPLE);
        gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

        frameBuffer = new FrameBuffer(gl, Config.WIDTH, Config.HEIGHT);

        Texture texture = new Texture(gl);
        texture.bind(gl);
        texture.attach(gl, GL4.GL_RGBA16F, Config.WIDTH, Config.HEIGHT, GL4.GL_RGBA, null);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        texture.clear(gl);

        frameBuffer.attach(gl, texture);
        frameBuffer.clear(gl);

        pe = new ParticleEmitter(gl);

        quad = new Quad(gl);
        quad.setTexture(texture);

        camera.setEye(0, 5, 5);
    }

    @Override
    public void update(long t) {
        ticks += 1;
        totalElapsed += t;

        double angle = (totalElapsed % 5000) / 5000. * 360.;
        double eyeX = 5 * Math.cos(Math.toRadians(angle));
        double eyeZ = 5 * Math.sin(Math.toRadians(angle));
        camera.setEye(eyeX, 5, eyeZ);

        pe.update(t);
    }

    @Override
    public void draw(GL4 gl) {
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
