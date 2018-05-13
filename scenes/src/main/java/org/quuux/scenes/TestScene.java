package org.quuux.scenes;

import org.quuux.opengl.entities.Model;
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

import java.util.logging.Logger;


public class TestScene extends Scene {
    private static final Logger LOGGER = Logger.getLogger( TestScene.class.getName() );

    long ticks, totalElapsed;

    FrameBuffer frameBuffer;
    ParticleEmitter pe = new ParticleEmitter();
    Quad quad = new Quad();
    Texture2D texture = new Texture2D();

    Model model = Model.load("models/sphere.obj");

    Command initializeCommand;
    Command drawCommand;

    public TestScene(int width, int height) {
        frameBuffer = new FrameBuffer(width, height);
    }

    @Override
    public Command initialize() {
        quad.setTexture(texture);
        Camera.getCamera().setEye(0, 100, 500);

        if (initializeCommand == null) {
            CommandList rv = new CommandList();
            rv.add(new ClearColor(0, 0, 0, 1));
            //rv.add(new GenerateFramebuffer(frameBuffer, texture));
            State ctx = new BatchState(new Enable(Enable.Capability.BLEND), new Enable(Enable.Capability.DEPTH_TEST));
            rv.add(ctx);

            ctx.add(new BlendFunc(BlendFunc.Factor.SRC_ALPHA, BlendFunc.Factor.ONE_MINUS_SRC_ALPHA));
            ctx.add(new DepthFunc(DepthFunc.Function.LESS));
            //rv.add(pe.initialize());
            rv.add(model.initialize());

            //rv.add(quad.initialize());
            initializeCommand = rv;
        }

        return initializeCommand;
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
        if (drawCommand == null) {
            CommandList rv = new CommandList();

            BatchState ctx = new BatchState(
                    new Enable(Enable.Capability.BLEND),
                    new Enable(Enable.Capability.DEPTH_TEST),
                    new Enable(Enable.Capability.MULTISAMPLE),
                    new Enable(Enable.Capability.POINT_SIZE)
                    //new BindFramebuffer(frameBuffer)
            );
            ctx.add(new Clear(Clear.Mode.COLOR_BUFFER, Clear.Mode.DEPTH_BUFFER));
            //ctx.add(pe.draw());
            ctx.add(model.draw());
            rv.add(ctx);

            // render texture
//            ctx = new BatchState(
//                    new Enable(Enable.Capability.BLEND),
//                    new Enable(Enable.Capability.MULTISAMPLE)
//            );
//            ctx.add(new Clear(Clear.Mode.COLOR_BUFFER));
            //ctx.add(quad.draw());
            //rv.add(ctx);

            drawCommand = rv;
        }

        return drawCommand;
    }
}
