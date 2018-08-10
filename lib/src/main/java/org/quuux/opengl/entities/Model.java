package org.quuux.opengl.entities;

import org.quuux.opengl.lib.Material;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.util.ResourceUtil;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjUtils;


public class Model implements Entity {
    public EntityGroup meshes = new EntityGroup();

    protected Model() {

    }

    @Override
    public void update(long t) {
        meshes.update(t);
    }

    @Override
    public Command initialize() {
        return meshes.initialize();
    }

    @Override
    public Command dispose() {
        return meshes.dispose();
    }

    @Override
    public Command draw() {
        return meshes.draw();
    }

    public static Model load(Material material, String name) {
        Obj obj = ResourceUtil.loadObj(name);
        if (obj == null)
            return null;

        System.out.println("loaded model: " + ObjUtils.createInfoString(obj));

        Model model = new Model();
        model.meshes.add(Mesh.fromObj(material, ObjUtils.convertToRenderable(obj)));

        return model;
    }
}
