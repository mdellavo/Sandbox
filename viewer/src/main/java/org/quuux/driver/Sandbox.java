package org.quuux.driver;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.scenes.Scene;
import org.quuux.scenes.TestScene;

class Sandbox implements KeyListener, GLEventListener {

    Scene scene;
    JOGLRenderer renderer = new DebugRenderer();
    long lastUpdate;

    public static void main(String[] args) {

        GLProfile glProfile = GLProfile.get(Config.GL_PROFILE);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(8);

        GLWindow window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(Config.WIDTH, Config.HEIGHT);

        window.setVisible(true);

        Scene scene = new TestScene(Config.WIDTH, Config.HEIGHT);
        Sandbox sandbox = new Sandbox(scene);

        window.addGLEventListener(sandbox);
        window.addKeyListener(sandbox);

        final FPSAnimator animator = new FPSAnimator(window, 60);
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

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        System.out.println(String.format("OpenGL Version: %s", gl.glGetString(GL.GL_VERSION)));

        Scene.getScene().setup();

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        renderer.setGL(drawable.getGL());
        Command command = Scene.getScene().dispose();
        command.run(renderer);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        renderer.setGL(drawable.getGL());

        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        lastUpdate = now;

        Scene.getScene().dispatchUpdate(elapsed);

        Command displayList = Scene.getScene().dispatchDraw();
        displayList.run(renderer);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
        Camera.getCamera().setProjection(45, (double)width/(double)height, .1, 1000.);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}