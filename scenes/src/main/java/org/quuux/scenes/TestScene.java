package org.quuux.scenes;

import org.quuux.opengl.entities.ParticleEmitter;
import org.quuux.opengl.entities.Quad;
import org.quuux.opengl.lib.FrameBuffer;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.scenes.Scene;


public class TestScene extends Scene {

    private final int width;
    private final int height;
    long ticks, totalElapsed;

    FrameBuffer frameBuffer;
    ParticleEmitter pe = new ParticleEmitter();
    Quad quad = new Quad();
    Texture2D texture = new Texture2D();

    public TestScene(int width, int height) {
        this.width = width;
        this.height = height;

        frameBuffer = new FrameBuffer(width, height);
    }

    private State newSceneContext() {
        return new BatchState(
                new Enable(Enable.Capability.BLEND),
                new Enable(Enable.Capability.DEPTH_TEST),
                //new Enable(Enable.Capability.MULTISAMPLE),
                //new Enable(Enable.Capability.POINT_SIZE),
                new BindTexture(texture),
                new ActivateTexture(0),
                new BindFramebuffer(frameBuffer)
        );
    }

    @Override
    public Command initialize() {
        quad.setTexture(texture);
        Camera.getCamera().setEye(0, 5, 5);


        CommandList rv = new CommandList();
        rv.add(new ClearColor(0, 0, 0, 1));
        rv.add(new GenerateTexture2D(texture));
        rv.add(new GenerateFramebuffer(frameBuffer));
        rv.add(new LoadTexture2D(texture, LoadTexture2D.Format.RGBA16F, width, height, LoadTexture2D.Format.RGBA, null, LoadTexture2D.Filter.LINEAR, LoadTexture2D.Filter.LINEAR));
        rv.add(new AttachFramebuffer(frameBuffer, texture));

        State ctx = newSceneContext();
        rv.add(ctx);

        ctx.add(new BlendFunc(BlendFunc.Factor.SRC_ALPHA, BlendFunc.Factor.ONE_MINUS_SRC_ALPHA));
        ctx.add(new DepthFunc(DepthFunc.Function.LESS));
        return rv;
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
    public Command draw() {
        CommandList rv = new CommandList();

        // pass 1 - render particles
        BindFramebuffer fbCommands = new BindFramebuffer(frameBuffer);
        fbCommands.add(new Clear(Clear.Mode.COLOR_BUFFER, Clear.Mode.DEPTH_BUFFER));
        fbCommands.add(pe.draw());
        rv.add(fbCommands);

        // render texture
        rv.add(new Clear(Clear.Mode.COLOR_BUFFER, Clear.Mode.DEPTH_BUFFER));
        rv.add(quad.draw());

        return rv;
    }
}
