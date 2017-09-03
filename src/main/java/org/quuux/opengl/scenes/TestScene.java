package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import org.joml.Vector3d;
import org.quuux.opengl.entities.ParticleEmitter;
import org.quuux.opengl.entities.Quad;

public class TestScene extends Scene {

    long ticks, totalElapsed;

    @Override
    public void setup(GL4 gl) {
        super.setup(gl);

        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL4.GL_BLEND);
        gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthFunc(GL4.GL_LESS);

        gl.glEnable(GL4.GL_MULTISAMPLE);

        Quad quad = new Quad(new Vector3d(0, 0, -3), gl);
        addChild(quad);

        ParticleEmitter pe = new ParticleEmitter(new Vector3d(), gl);
        addChild(pe);

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

        super.update(t);

        if (totalElapsed % 1000 == 0) {
            double avgElapsed = totalElapsed / ticks;
            System.out.println("avg elapsed per tick: " + avgElapsed);
        }
    }

    @Override
    public void draw(GL4 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        super.draw(gl);
    }
}
