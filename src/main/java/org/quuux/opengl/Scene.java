package org.quuux.opengl;

import com.jogamp.opengl.GL4;

public abstract class Scene extends EntityGroup {
    private static Scene instance;

    public Camera camera = new Camera();

    public Scene() {
        setScene(this);
    }

    public void setup(GL4 gl) {

    }

    public static Scene getScene() {
        return instance;
    }

    public static void setScene(Scene scene) {
        System.out.println("set scene " + scene.getClass().getSimpleName());
        instance = scene;
    }

    public Camera getCamera() {
        return camera;
    }
}
