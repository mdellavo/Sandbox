package org.quuux.opengl.entities;

import org.joml.Vector3f;

public class Material {
    Vector3f ambient, diffuse, specular;
    float shininess;

    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
