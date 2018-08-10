package org.quuux.opengl.lib;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.State;

public class Material {
    public String key;
    public Texture diffuse, specular;

    public float shininess;

    public Material(String key, Texture diffuse, Texture specular, float shininess) {
        this.key = key;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(diffuse.initialize(0));
        rv.add(specular.initialize(1));
        return rv;
    }

    public State bind() {
        BatchState rv = new BatchState(
                diffuse.bind(0),
                specular.bind(1)
        );
        return rv;
    }

    public static String getTextureKey(String key, String type) {
        return String.format("%s-%s", key, type);
    }

    public static Texture loadTexture(String key, String type) {
        return new Texture2D(getTextureKey(key, type));
    }

    public static Material load( String key, float shininess) {
        Texture diffuse = loadTexture(key, "diffuse");
        Texture specular = loadTexture(key, "specular");
        return new Material(key, diffuse, specular, shininess);
    }
}
