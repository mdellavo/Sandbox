package org.quuux.opengl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import org.quuux.opengl.scenes.Scene;
import org.quuux.opengl.scenes.TestScene;

class Sandbox implements KeyListener, GLEventListener {

    Scene scene;

    public static void main(String[] args) {

        GLProfile glProfile = GLProfile.get(Config.GL_PROFILE);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(8);

        GLWindow window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(Config.WIDTH, Config.HEIGHT);

        window.setVisible(true);

        Scene scene = new TestScene();
        Sandbox sandbox = new Sandbox(scene);

        window.addGLEventListener(sandbox);
        window.addKeyListener(sandbox);

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.setUpdateFPSFrames(60 * 5, System.out);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
            }
        });

        animator.start();
    }

    private Sandbox(Scene scene) {
        this.scene = scene;
    }

    long lastUpdate;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        System.out.println(String.format("OpenGL Version: %s", gl.glGetString(GL.GL_VERSION)));

        Scene.getScene().setup(gl);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        Scene.getScene().dispose(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        lastUpdate = now;

        Scene.getScene().dispatchUpdate(elapsed);

        Scene.getScene().dispatchDraw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
        Scene.getScene().getCamera().setProjection(45, (double)width/(double)height, .1, 1000.);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}