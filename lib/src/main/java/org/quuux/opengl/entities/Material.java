package org.quuux.opengl.entities;

import org.quuux.opengl.lib.Texture;

public class Material {
    Texture diffuse, specular;

    float shininess;

    public Material(Texture diffuse, Texture specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

}
