package org.quuux.opengl.scenes;

import com.jogamp.opengl.GL4;
import org.quuux.opengl.entities.EntityGroup;
import org.quuux.opengl.util.Log;

public class Scene extends EntityGroup {
    private static final long FREQUENCY = 100;
    private static Scene instance;

    private long numUpdates, numDraws, totalUpdateTime, totalDrawTime;

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

    public void dispatchUpdate(long t) {
        long t1 = System.currentTimeMillis();
        this.update(t);
        long t2 = System.currentTimeMillis();

        long delta = t2-t1;
        totalUpdateTime += delta;
        numUpdates++;

        if (numUpdates > FREQUENCY) {
            Log.out("avg update time = %.02fms", (float)totalUpdateTime / (float)numUpdates);
            totalUpdateTime = numUpdates = 0;
        }
    }

    public void dispatchDraw(GL4 gl) {
        long t1 = System.currentTimeMillis();
        this.draw(gl);
        long t2 = System.currentTimeMillis();

        long delta = t2-t1;
        totalDrawTime += delta;
        numDraws++;

        if (numDraws > FREQUENCY) {
            Log.out("avg draw time = %.02fms", (float)totalDrawTime / (float)numDraws);
            totalDrawTime = numDraws = 0;
        }
    }
}
