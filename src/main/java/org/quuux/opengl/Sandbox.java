package org.quuux.opengl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.io.File;
import java.net.URL;

class Sandbox implements KeyListener, GLEventListener {

    public static void main(String[] args) {
        System.out.println("Loading sandbox...");
        new Sandbox().setup();
    }

    static GLWindow window;
    static Animator animator;

    long lastUpdate;

    EntityGroup root = new EntityGroup();

    private void setup() {

        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(1024, 768);

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

        ParticleSystem ps = new ParticleSystem(new Vec3F());
        root.addChild(ps);

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        System.out.println(String.format("OpenGL Version: %s", gl.glGetString(GL.GL_VERSION)));

        gl.glClearColor(0, 0, 0, 0);

        root.initialize(gl);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        root.dispose(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        updateAll();

        GL4 gl = drawable.getGL().getGL4();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        root.draw(gl);
    }

    private void updateAll() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        root.update(elapsed);
        long took = System.currentTimeMillis() - now;
        // System.out.println(String.format("update took %s", took));
        lastUpdate = now;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}