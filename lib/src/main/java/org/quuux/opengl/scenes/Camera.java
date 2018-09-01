package org.quuux.opengl.scenes;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;


public class Camera {

    public enum Direction {
        FORWARD,
        BACK,
        LEFT,
        RIGHT,
    }

    public final Vector3d WORLD_UP = new Vector3d(0, 1, 0);

    public double fov, aspectRatio, zNear, zFar;

    double yaw = -90.;
    double pitch = 0;

    public Vector3d position = new Vector3d();
    public Vector3d front = new Vector3d(0, -1, -1);
    public Vector3d up = new Vector3d();
    public Vector3d right = new Vector3d();

    public Matrix4d projectionMatrix = new Matrix4d();
    public Matrix4d viewMatrix = new Matrix4d();

    public Matrix4d scratch = new Matrix4d();

    public Camera() { }


    public void setPosition(double x, double y, double z) {
        position.set(x, y, z);
        update();
    }

    public void setProjection(double fov, double aspectRatio, double zNear, double zFar) {
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;
        update();
    }

    public void move(Direction direction, double speed) {

        Vector3d tmp = new Vector3d();

        if (direction == Direction.FORWARD) {
            tmp.set(front);
            tmp.mul(speed);
            position.add(tmp);
        } else if (direction == Direction.BACK) {
            tmp.set(front);
            tmp.mul(speed);
            position.sub(tmp);
        } else if (direction == Direction.LEFT) {
            tmp.set(right);
            tmp.mul(speed);
            position.sub(tmp);
        } else if (direction == Direction.RIGHT) {
            tmp.set(right);
            tmp.mul(speed);
            position.add(tmp);
        }
        update();
    }

    public void rotate(double x, double y) {
        yaw += x;
        pitch += y;
        update();
    }

    public void updateProjectionMatrix() {
        projectionMatrix.identity().perspective(fov, aspectRatio, zNear, zFar);
    }

    public void updateViewMatrix() {
        Vector3d center = new Vector3d(position);
        center.add(front);

        viewMatrix.identity().lookAt(
                position.x, position.y, position.z,
                center.x, center.y, center.z,
                up.x, up.y, up.z
        );
    }

    private void update() {
        double x = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double y = Math.sin(Math.toRadians(pitch));
        double z = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

        front.set(x, y, z).normalize();

        right.set(front).cross(WORLD_UP).normalize();
        up.set(right).cross(front).normalize();

        updateProjectionMatrix();
        updateViewMatrix();
    }

    public void modelViewProjectionMatrix(Matrix4d model, Matrix4f dest) {
        dest.set(scratch.set(projectionMatrix).mul(viewMatrix).mul(model));
    }

}