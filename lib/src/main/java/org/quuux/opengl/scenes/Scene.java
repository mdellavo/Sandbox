package org.quuux.opengl.scenes;

import org.quuux.opengl.entities.EntityGroup;
import org.quuux.opengl.renderer.Command;

public class Scene extends EntityGroup {
    private static Scene instance;

    public Scene() {
        setScene(this);
    }

    public static Scene getScene() {
        return instance;
    }

    public static void setScene(Scene scene) {
        System.out.println("set scene " + scene.getClass().getSimpleName());
        instance = scene;
    }

    public void dispatchUpdate(long t) {
        this.update(t);
    }

    public Command dispatchDraw() {
        return this.draw();
    }
}
