package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL4;
import org.quuux.opengl.entities.EntityGroup;
import org.quuux.opengl.util.Log;

public class Scene extends EntityGroup {
    private static Scene instance;

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

    public void dispatchUpdate(long t) {
        this.update(t);
    }

    public void dispatchDraw(GL4 gl) {
        this.draw(gl);
    }
}
