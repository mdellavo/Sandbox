package org.quuux.opengl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import org.joml.Vector3d;

class Sandbox implements KeyListener, GLEventListener {

    public static void main(String[] args) {
        System.out.println("Loading sandbox...");
        new Sandbox().setup();
    }

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    static GLWindow window;
    static Animator animator;

    long lastUpdate, totalElapsed;

    EntityGroup root = new EntityGroup();
    Camera camera = new Camera();

    private void setup() {
        camera.setEye(0, 5, 5);

        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle("OpenGL Sandbox");
        window.setSize(WIDTH, HEIGHT);

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

        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL4.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glEnable(GL4.GL_MULTISAMPLE);

        Quad quad = new Quad(new Vector3d(0, 0, -3), gl);
        root.addChild(quad);

        ParticleSystem ps = new ParticleSystem(new Vector3d(), gl);
        root.addChild(ps);

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

        root.draw(gl, camera);
    }

    private void updateAll() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        totalElapsed += elapsed;
        lastUpdate = now;

//        double angle = (totalElapsed % 5000) / 5000. * 360.;
//        double eyeX = 5 * Math.cos(Math.toRadians(angle));
//        double eyeZ = 5 * Math.sin(Math.toRadians(angle));
//        camera.setEye(eyeX, 5, eyeZ);

        root.update(elapsed);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);

        camera.setProjection(45, (double)width/(double)height, .1, 1000.);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}