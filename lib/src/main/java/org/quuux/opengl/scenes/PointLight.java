package org.quuux.opengl.scenes;

import org.joml.Vector3f;

public class PointLight extends Light {
    public Vector3f position = new Vector3f();
    public float constant, linear, quadratic;
}
