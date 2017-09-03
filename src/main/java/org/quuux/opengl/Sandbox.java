package org.quuux.opengl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

class Sandbox implements KeyListener, GLEventListener {

    public static void main(String[] args) {
        System.out.println("Loading sandbox...");
        new TestScene();
        new Sandbox().setup();
    }

    static GLWindow window;
    static Animator animator;

    long lastUpdate;

    private void setup() {
        GLProfile glProfile = GLProfile.get(Config.GL_PROFILE);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(Config.WIDTH, Config.HEIGHT);

        window.setVisible(true);

        window.addGLEventListener(this);
        window.addKeyListener(this);

        animator = new Animator(window);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(1);
            }
        });
    }

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
        Scene.getScene().update(elapsed);
        Scene.getScene().draw(gl);
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