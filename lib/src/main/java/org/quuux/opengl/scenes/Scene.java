package org.quuux.opengl.scenes;

import org.quuux.opengl.entities.Entity;
import org.quuux.opengl.entities.EntityGroup;
import org.quuux.opengl.renderer.Command;

import java.util.ArrayList;
import java.util.List;


public class Scene implements Entity {
    private static Scene instance;

    public Camera camera = new Camera();
    public DirectionalLight directionalLight = new DirectionalLight();
    public List<PointLight> pointLights = new ArrayList<>();
    public EntityGroup entities = new EntityGroup();

    protected Scene() {
        set(this);
    }

    public static Scene get() {
        return instance;
    }

    public static void set(Scene scene) {
        System.out.println("set scene " + scene.getClass().getSimpleName());
        instance = scene;
    }

    public Camera getCamera() {
        return camera;
    }

    public void dispatchUpdate(long t) {
        this.update(t);
    }

    public Command dispatchDraw() {
        return this.draw();
    }

    @Override
    public void update(final long t) {
        entities.update(t);
    }

    @Override
    public Command initialize() {
        return entities.initialize();
    }

    @Override
    public Command dispose() {
        return entities.dispose();
    }

    @Override
    public Command draw() {
        return entities.draw();
    }
}
