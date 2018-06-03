package org.quuux.opengl.entities;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.util.ResourceUtil;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjGroup;
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

    public static Model load(String name) {
        Obj obj = ResourceUtil.loadObj(name);
        if (obj == null)
            return null;

        System.out.println("loaded model: " + ObjUtils.createInfoString(obj));

        Model model = new Model();
        for (int i=0; i<obj.getNumGroups(); i++) {
            ObjGroup group = obj.getGroup(i);
            if (group.getNumFaces() > 0) {
                Obj groupObj = ObjUtils.groupToObj(obj, group, null);
                model.meshes.add(Mesh.create(ObjUtils.convertToRenderable(groupObj)));
            }
        }

        return model;
    }
}
