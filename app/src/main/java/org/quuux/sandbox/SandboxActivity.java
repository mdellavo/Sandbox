package org.quuux.sandbox;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.scenes.Scene;
import org.quuux.scenes.TestScene;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class SandboxActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SceneRenderer renderer = new SceneRenderer();

        GLSurfaceView view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(3);
        view.setRenderer(renderer);

        setContentView(view);
    }

    class SceneRenderer implements GLSurfaceView.Renderer {

        Scene scene;
        long lastUpdate;

        AndroidGL2Renderer renderer = new AndroidGL2Renderer();

        @Override
        public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
            System.out.println(String.format("OpenGL Version: %s", gl.glGetString(GL10.GL_VERSION)));
        }

        @Override
        public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);

            scene = new TestScene(width, height);
            Command command = scene.initialize();
            command.run(renderer);

            lastUpdate = System.currentTimeMillis();
        }

        @Override
        public void onDrawFrame(final GL10 gl) {

            long now = System.currentTimeMillis();
            long elapsed = now - lastUpdate;
            lastUpdate = now;

            scene.dispatchUpdate(elapsed);
            Command displayList = scene.dispatchDraw();
            displayList.run(renderer);

        }
    }

}
