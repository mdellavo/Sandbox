package org.quuux.opengl.lib;

import org.joml.Vector3f;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.State;
import org.quuux.opengl.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public Texture diffuse, specular, normal;

    public float shininess;

    public Material(Texture diffuse, Texture specular, Texture normal, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.normal = normal;
    }

    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(diffuse.initialize(0));
        rv.add(specular.initialize(1));
        if (normal != null)
            rv.add(normal.initialize(2));
        return rv;
    }

    public State bind() {
        List<State> states = new ArrayList<>();
        states.add(diffuse.bind(0));
        states.add(specular.bind(1));

        if (normal != null)
            states.add(normal.bind(2));

        State[] args = new State[states.size()];
        states.toArray(args);

        BatchState rv = new BatchState(args);
        return rv;
    }

    public static String getTextureKey(String key, String type) {
        return String.format("textures/%s-%s.png", key, type);
    }

    public static Texture loadTexture(String key, String type) {
        ResourceUtil.Bitmap bitmap = ResourceUtil.getPNGResource(getTextureKey(key, type));
        return bitmap != null ? new Texture2D(bitmap) : null;
    }

    public static Material load(String key, float shininess) {
        Texture diffuse = loadTexture(key, "diffuse");
        Texture specular = loadTexture(key, "specular");
        Texture normal = loadTexture(key, "normal");
        return new Material(diffuse, specular, normal, shininess);
    }

    public static Material color(Vector3f color, float shininess) {
        ResourceUtil.Bitmap diffuse = ResourceUtil.getColor(color, 1);
        ResourceUtil.Bitmap specular = ResourceUtil.getColor(new Vector3f(1, 1, 1), 1);
        //ResourceUtil.Bitmap normal = ResourceUtil.getColor(new Vector3f(0, 0, 1), 1);
        return new Material(new Texture2D(diffuse), new Texture2D(specular), null, shininess);
    }
}
