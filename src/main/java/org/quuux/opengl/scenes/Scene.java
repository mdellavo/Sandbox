package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL4;
import org.quuux.opengl.entities.EntityGroup;

public abstract class Scene extends EntityGroup {
    private static Scene instance;

    public Camera camera = new Camera();

    public Scene() {
        setScene(this);
    }

    public void setup(GL4 gl) { }

    public static Scene getScene() {
        return instance;
    }

    public static void setScene(Scene scene) {
        if (instance != null) {
            throw new RuntimeException("Scene is already set!");
        }

        System.out.println("set scene " + scene.getClass().getSimpleName());
        instance = scene;
    }

    public Camera getCamera() {
        return camera;
    }
}
