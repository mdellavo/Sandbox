package org.quuux.opengl.scenes;

import org.quuux.opengl.entities.EntityGroup;
import org.quuux.opengl.renderer.Command;

import java.util.ArrayList;
import java.util.List;


public class Scene extends EntityGroup {
    private static Scene instance;

    public Camera camera = new Camera();
    public DirectionalLight directionalLight = new DirectionalLight();
    public List<PointLight> pointLights = new ArrayList<>();

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

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    public void dispatchUpdate(long t) {
        this.update(t);
    }

    public Command dispatchDraw() {
        return this.draw();
    }
}
