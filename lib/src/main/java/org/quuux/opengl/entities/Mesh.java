package org.quuux.opengl.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;

public class Mesh implements Entity {

    public static class Vertex {
        Vector3f position, normal;
        Vector2f texCoords;
    }

    List<Vertex> vertex = new ArrayList<>();
    IntBuffer indicies;

    List<Texture2D> textures = new ArrayList<>();

    FloatBuffer vertexBuffer;

    BufferObject vbo = new BufferObject();
    BufferObject ebo = new BufferObject();
    VAO vao = new VAO();

    Command displayList;

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateBuffer(ebo));
        return rv;
    }

    @Override
    public Command dispose() {
        return null;
    }

    @Override
    public Command draw() {
        if (displayList == null) {
            BatchState rv = new BatchState(new BindArray(vao), new BindBuffer(BufferType.ArrayBuffer, vbo), new BindBuffer(BufferType.ElementArrayBuffer, ebo));
            rv.add(new BufferData(BufferType.ArrayBuffer, vertexBuffer.capacity() * 4, vertexBuffer, BufferData.Usage.StaticDraw));
            rv.add(new BufferData(BufferType.ElementArrayBuffer, indicies.capacity() * 4, indicies, BufferData.Usage.StaticDraw));
            displayList = rv;
        }

        return displayList;
    }

    @Override
    public void update(long t) {

    }

    public static Mesh create(Obj obj) {
        Mesh mesh = new Mesh();

        mesh.indicies = ObjData.getFaceNormalIndices(obj);

        for(int i=0; i<obj.getNumVertices(); i++) {
            Mesh.Vertex vert = new Mesh.Vertex();

            FloatTuple position = obj.getVertex(i);
            vert.position.set(position.getX(), position.getY(), position.getZ());

            FloatTuple normal = obj.getNormal(i);
            vert.normal.set(normal.getX(), normal.getY(), normal.getZ());

            FloatTuple texCoord = obj.getTexCoord(i);
            vert.texCoords.set(texCoord.getX(), texCoord.getY());

            mesh.vertex.add(vert);
        }

        return mesh;
    }
}
