package org.quuux.opengl;

import org.joml.Matrix4d;
import org.joml.Matrix4f;


public class Camera {

    double fov, aspectRatio, zNear, zFar;
    Vec3 eye = new Vec3(), center = new Vec3(), up = new Vec3(0, 1, 0);

    Matrix4d projectionMatrix = new Matrix4d(),
            viewMatrix = new Matrix4d();

    public void setEye(double x, double y, double z) {
        eye.set(x, y, z);
        updateViewMatrix();
    }

    public void setCenter(float x, float y, float z) {
        center.set(x, y, z);
        updateViewMatrix();
    }

    public void setProjection(double fov, double aspectRatio, double zNear, double zFar) {
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;
        updateProjectionMatrix();

    }

    public void updateProjectionMatrix() {
        projectionMatrix.identity().perspective(fov, aspectRatio, zNear, zFar);
    }

    public void updateViewMatrix() {
        viewMatrix.identity().lookAt(
                eye.x, eye.y, eye.z,
                center.x, center.y, center.z,
                up.x, up.y, up.z
        );
    }

    public void modelViewProjectionMatrix(Matrix4d model, Matrix4f dest) {
        dest.set(new Matrix4d(projectionMatrix).mul(viewMatrix).mul(model));
    }
}