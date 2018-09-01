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

    static GLWindow window;
    static FPSAnimator animator;

    Scene scene;
    JOGLRenderer renderer = new JOGLRenderer();
    long lastUpdate;

    public static void main(String[] args) {

        GLProfile glProfile = GLProfile.get(Config.GL_PROFILE);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(8);

        window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(Config.WIDTH, Config.HEIGHT);

        window.setVisible(true);

        Scene scene = new TestScene();
        Sandbox sandbox = new Sandbox(scene);

        window.addGLEventListener(sandbox);
        window.addKeyListener(sandbox);

        animator = new FPSAnimator(window, 60);
        animator.setUpdateFPSFrames(60 * 5, System.out);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                exit();
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
        renderer.setGL(gl);

        System.out.println(String.format("OpenGL Version: %s", gl.glGetString(GL.GL_VERSION)));

        Command command = scene.initialize();
        command.run(renderer);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        Command command = scene.dispose();
        command.run(renderer);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        lastUpdate = now;

        scene.dispatchUpdate(elapsed);

        Command displayList = scene.dispatchDraw();
        displayList.run(renderer);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
        Scene.get().getCamera().setProjection(45, (double)width/(double)height, 1, 1000.);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        double inc = 1f;

        if ( key == KeyEvent.VK_LEFT ) {
            scene.camera.rotate(-inc, 0);
        } else if ( key == KeyEvent.VK_RIGHT ) {
            scene.camera.rotate(inc, 0);
        } else if ( key == KeyEvent.VK_DOWN) {
            scene.camera.rotate(0, -inc);
        } else if ( key == KeyEvent.VK_UP ) {
            scene.camera.rotate(0, inc);
        } else if ( key == KeyEvent.VK_W ) {
            scene.camera.move(Camera.Direction.FORWARD, inc);
        } else if ( key == KeyEvent.VK_A ) {
            scene.camera.move(Camera.Direction.LEFT, inc);
        } else if ( key == KeyEvent.VK_S ) {
            scene.camera.move(Camera.Direction.BACK, inc);
        } else if ( key == KeyEvent.VK_D ) {
            scene.camera.move(Camera.Direction.RIGHT, inc);
        } else if (key == KeyEvent.VK_ESCAPE) {
            exit();
        }

        //System.out.println(String.format("center = %s / eye = %s", scene.camera.center, scene.camera.eye));

        scene.camera.updateViewMatrix();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void exit() {
        window.setVisible(false);
        animator.stop();
        System.exit(0);
    }
}