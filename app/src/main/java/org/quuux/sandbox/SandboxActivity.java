package org.quuux.sandbox;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.scenes.Scene;
import org.quuux.scenes.TestScene;
import org.quuux.feller.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class SandboxActivity extends Activity {

    private static final String TAG = Log.buildTag(SandboxActivity.class);

    SceneRenderer renderer = new SceneRenderer();
    private static final long INTERVAL = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView view = new GL3SurfaceView(this);
        setContentView(view);
    }

    class GL3SurfaceView extends GLSurfaceView {
        public GL3SurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(true);
            setEGLContextClientVersion(3);
            setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            getHolder().setFormat(PixelFormat.TRANSLUCENT);
            setRenderer(renderer);
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }
    }

    class SceneRenderer implements GLSurfaceView.Renderer {

        Scene scene;
        long lastUpdate;

        long drawCount;
        long totalDrawTime;

        AndroidGLRenderer renderer = new AndroidGLRenderer();

        Command displayList;

        @Override
        public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
            Log.d(TAG, "OpenGL Version: %s (%s)", gl.glGetString(GL10.GL_VERSION), config);
        }

        @Override
        public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
            Log.d(TAG, "viewport %s x %s", width, height);

            GLES20.glViewport(0, 0, width, height);
            Camera.getCamera().setProjection(45, (double)width/(double)height, .1, 1000.);

            scene = new TestScene(width, height);
            Command command = scene.initialize();
            command.run(renderer);

            lastUpdate = System.currentTimeMillis();

            drawCount = totalDrawTime = 0;

            displayList = scene.dispatchDraw();
        }

        @Override
        public void onDrawFrame(final GL10 gl) {

            long now = System.currentTimeMillis();
            long elapsed = now - lastUpdate;

            scene.dispatchUpdate(elapsed);

            displayList.run(renderer);

            lastUpdate = now;

            drawCount += 1;
            totalDrawTime += elapsed;

            if (totalDrawTime > INTERVAL) {
                // r * t = d
                Log.d(TAG, "total frames: %s / fps: %.02f", drawCount, (float)drawCount / (float)totalDrawTime * 1000);
                drawCount = totalDrawTime = 0;
            }
        }
    }

}
