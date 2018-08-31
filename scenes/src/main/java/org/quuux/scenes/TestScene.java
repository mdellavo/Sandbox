package org.quuux.scenes;

import org.joml.Vector3d;
import org.quuux.opengl.entities.Entity;
import org.quuux.opengl.lib.Material;
import org.quuux.opengl.entities.Mesh;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.*;
import org.quuux.opengl.renderer.states.*;
import org.quuux.opengl.scenes.PointLight;
import org.quuux.opengl.scenes.Scene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestScene extends Scene {

    long ticks, totalElapsed;

    Material worldmap = Material.load("world", 1f);
    Material brick = Material.load("brick", 1f);

    Mesh globe = Mesh.createIcoSphere(worldmap, 20, 3);
    Mesh ground = Mesh.createQuad(brick);
    List<Mesh> bulbs = new ArrayList<>();

    CommandList initializeCommand;
    CommandList drawCommand;

    public TestScene() {
        super();

        camera.setEye(0, 50, 50);

        directionalLight.direction.set(-0.2f, -1.0f, -0.3f);
        directionalLight.ambient.set(.1f, 0.1f, 0.1f);
        directionalLight.diffuse.set(0.3f, 0.3f, 0.3f);
        directionalLight.specular.set(0.2f, 0.2f, 0.2f);

        double lightDistance = 20.1;

        PointLight pointLight1 = new PointLight();
        pointLights.add(pointLight1);
        float p1X = (float) (lightDistance * Math.cos(Math.toRadians(0)));
        float p1Z = (float) (lightDistance * Math.sin(Math.toRadians(0)));
        pointLight1.position.set(p1X, (float) lightDistance, p1Z);
        pointLight1.ambient.set(0, 0, 0.05f);
        pointLight1.diffuse.set(0, 0, 0.8f);
        pointLight1.specular.set(0, 0, 1.0f);
        pointLight1.constant = 1.0f;
        pointLight1.linear = .9f;
        pointLight1.quadratic = .32f;

        PointLight pointLight2 = new PointLight();
        pointLights.add(pointLight2);
        float p2X = (float) (lightDistance * Math.cos(Math.toRadians(120)));
        float p2Z = (float) (lightDistance * Math.sin(Math.toRadians(120)));
        pointLight2.position.set(p2X, (float) lightDistance, p2Z);
        pointLight2.ambient.set(0, 0.05f, 0);
        pointLight2.diffuse.set(0, 0.8f, 0);
        pointLight2.specular.set(0, 1.0f, 0);
        pointLight2.constant = 1.0f;
        pointLight2.linear = .9f;
        pointLight2.quadratic = .32f;

        PointLight pointLight3 = new PointLight();
        pointLights.add(pointLight3);
        float p3X = (float) (lightDistance * Math.cos(Math.toRadians(240)));
        float p3Z = (float) (lightDistance * Math.sin(Math.toRadians(240)));
        pointLight3.position.set(p3X, (float) lightDistance, p3Z);
        pointLight3.ambient.set(0.05f, 0, 0);
        pointLight3.diffuse.set(0.8f, 0, 0);
        pointLight3.specular.set(1.0f, 0, 0);
        pointLight3.constant = 1.0f;
        pointLight3.linear = .9f;
        pointLight3.quadratic = .32f;

        entities.add(globe);

        ground.model.translate(0, -30, 0);
        ground.model.rotate(Math.toRadians(90), 1, 0, 0);
        ground.model.scale(100);
        entities.add(ground);

        for (PointLight pointLight : pointLights) {
            Mesh bulb = Mesh.createCube(Material.color(pointLight.ambient, 1));
            bulb.model.translate(pointLight.position);
            entities.add(bulb);
            bulbs.add(bulb);
        }

    }

    @Override
    public Command initialize() {

        if (initializeCommand == null) {
            CommandList rv = new CommandList();

            rv.add(new ClearColor(0, 0, 0, 1));
            State ctx = new BatchState(new Enable(Enable.Capability.BLEND), new Enable(Enable.Capability.DEPTH_TEST));
            rv.add(ctx);

            ctx.add(new BlendFunc(BlendFunc.Factor.SRC_ALPHA, BlendFunc.Factor.ONE_MINUS_SRC_ALPHA));
            ctx.add(new DepthFunc(DepthFunc.Function.LESS));

            ctx.add(super.initialize());

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

        //camera.setEye(eyeX, 50, eyeZ);

        super.update(t);
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
            rv.add(ctx);
            ctx.add(new Clear(Clear.Mode.COLOR_BUFFER, Clear.Mode.DEPTH_BUFFER));
            ctx.add(super.draw());
            drawCommand = rv;
        }
        return drawCommand;
    }

    Comparator<Entity> entityComparator = new Comparator<Entity>() {

        Vector3d v1 = new Vector3d();
        Vector3d v2 = new Vector3d();

        @Override
        public int compare(final Entity e1, final Entity e2) {
            Mesh m1 = (Mesh)e1;
            Mesh m2 = (Mesh)e2;
            m1.model.getTranslation(v1);
            m2.model.getTranslation(v2);
            double d1 = v1.distance(camera.eye);
            double d2 = v2.distance(camera.eye);
            return Double.compare(d1, d2);
        }
    };
}
