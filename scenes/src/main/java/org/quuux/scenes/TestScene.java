package org.quuux.scenes;

import org.quuux.opengl.entities.Model;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;
import org.quuux.opengl.scenes.PointLight;
import org.quuux.opengl.scenes.Scene;


public class TestScene extends Scene {

    long ticks, totalElapsed;

    Model model = Model.load("models/sphere.obj");

    Command initializeCommand;
    Command drawCommand;

    @Override
    public Command initialize() {
        camera.setEye(0, 50, 50);

        directionalLight.direction.set(-0.2f, -1.0f, -0.3f);
        directionalLight.ambient.set(0.05f, 0.05f, 0.05f);
        directionalLight.diffuse.set(0.4f, 0.4f, 0.4f);
        directionalLight.specular.set(0.5f, 0.5f, 0.5f);

        PointLight pointLight1 = new PointLight();
        pointLights.add(pointLight1);
        float p1X = (float) (2.5f * Math.cos(Math.toRadians(0)));
        float p1Y = (float) (2.5f * Math.sin(Math.toRadians(0)));
        pointLight1.position.set(p1X, p1Y, 5);
        pointLight1.ambient.set(0.05f, 0.05f, 0.05f);
        pointLight1.diffuse.set(0.8f, 0.8f, 0.8f);
        pointLight1.specular.set(1.0f, 1.0f, 1.0f);
        pointLight1.constant = 1.0f;
        pointLight1.linear = .09f;
        pointLight1.quadratic = .032f;

        PointLight pointLight2 = new PointLight();
        pointLights.add(pointLight2);
        float p2X = (float) (2.5f * Math.cos(Math.toRadians(120)));
        float p2Y = (float) (2.5f * Math.sin(Math.toRadians(120)));
        pointLight2.position.set(p2X, p2Y, 5);
        pointLight2.ambient.set(0.05f, 0.05f, 0.05f);
        pointLight2.diffuse.set(0.8f, 0.8f, 0.8f);
        pointLight2.specular.set(1.0f, 1.0f, 1.0f);
        pointLight2.constant = 1.0f;
        pointLight2.linear = .09f;
        pointLight2.quadratic = .032f;

        PointLight pointLight3 = new PointLight();
        pointLights.add(pointLight3);
        float p3X = (float) (2.5f * Math.cos(Math.toRadians(240)));
        float p3Y = (float) (2.5f * Math.sin(Math.toRadians(240)));
        pointLight3.position.set(p3X, p3Y, 5);
        pointLight3.ambient.set(0.05f, 0.05f, 0.05f);
        pointLight3.diffuse.set(0.8f, 0.8f, 0.8f);
        pointLight3.specular.set(1.0f, 1.0f, 1.0f);
        pointLight3.constant = 1.0f;
        pointLight3.linear = .09f;
        pointLight3.quadratic = .032f;

        if (initializeCommand == null) {
            CommandList rv = new CommandList();
            rv.add(new ClearColor(0, 0, 0, 1));
            State ctx = new BatchState(new Enable(Enable.Capability.BLEND), new Enable(Enable.Capability.DEPTH_TEST));
            rv.add(ctx);

            ctx.add(new BlendFunc(BlendFunc.Factor.SRC_ALPHA, BlendFunc.Factor.ONE_MINUS_SRC_ALPHA));
            ctx.add(new DepthFunc(DepthFunc.Function.LESS));
            rv.add(model.initialize());

            initializeCommand = rv;
        }

        return initializeCommand;
    }

    @Override
    public void update(long t) {
        ticks += 1;
        totalElapsed += t;

        double angle = (totalElapsed % 5000) / 5000. * 360.;
        double eyeX = 50 * Math.cos(Math.toRadians(angle));
        double eyeZ = 50 * Math.sin(Math.toRadians(angle));

        camera.setEye(eyeX, 50, eyeZ);

        model.update(t);
    }

    @Override
    public Command draw() {
        if (drawCommand == null) {
            CommandList rv = new CommandList();

            BatchState ctx = new BatchState(
                    new Enable(Enable.Capability.BLEND),
                    new Enable(Enable.Capability.DEPTH_TEST),
                    new Enable(Enable.Capability.MULTISAMPLE)
            );
            ctx.add(new Clear(Clear.Mode.COLOR_BUFFER, Clear.Mode.DEPTH_BUFFER));
            ctx.add(model.draw());
            rv.add(ctx);

            drawCommand = rv;
        }

        return drawCommand;
    }
}
