package org.quuux.opengl.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.GenerateTexture2D;
import org.quuux.opengl.renderer.commands.LoadTexture2D;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.util.GLUtil;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;

public class Mesh implements Entity {

    public static class Vertex {
        Vector3f position = new Vector3f(),
                normal = new Vector3f();
        Vector2f texCoords = new Vector2f();
    }

    List<Vertex> vertex = new ArrayList<>();
    IntBuffer indicies;

    List<Texture2D> textures = new ArrayList<>();

    FloatBuffer vertexBuffer;

    BufferObject vbo = new BufferObject();
    BufferObject ebo = new BufferObject();
    VAO vao = new VAO();

    ShaderProgram shader = new ShaderProgram();
    Texture2D texture = new Texture2D();

    Command displayList;

    CommandList buildState() {
        BatchState rv = new BatchState(
                new UseProgram(shader),
                new BindArray(vao),
                new BindBuffer(BufferType.ArrayBuffer, vbo),
                new BindBuffer(BufferType.ElementArrayBuffer, ebo),
                new BindTexture(texture),
                new ActivateTexture(0)
        );
        return rv;
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateBuffer(ebo));
        rv.add(new GenerateTexture2D(texture));

        rv.add(ShaderProgram.build(shader,
                ResourceUtil.getStringResource("shaders/mesh.vert.glsl"),
                ResourceUtil.getStringResource("shaders/mesh.frag.glsl")));

        CommandList ctx = buildState();
        rv.add(ctx);

        ResourceUtil.DecodedImage image = ResourceUtil.getPNGResource("textures/particle1.png");
        ctx.add(new LoadTexture2D(texture, LoadTexture2D.Format.RGBA, image.width, image.height,  LoadTexture2D.Format.RGBA, image.buffer, LoadTexture2D.Filter.LINEAR, LoadTexture2D.Filter.LINEAR));

        ctx.add(new SetUniform(shader, "texture", SetUniform.Type.INT, 0));

        ctx.add(new VertexAttribPointer(0, 3, VertexAttribPointer.Type.Float, false, 8 * 4, 0));
        ctx.add(new EnableVertexAttribArray(0));

        ctx.add(new VertexAttribPointer(1, 3, VertexAttribPointer.Type.Float, false, 8 * 4, 3 * 4));
        ctx.add(new EnableVertexAttribArray(1));

        ctx.add(new VertexAttribPointer(2, 2, VertexAttribPointer.Type.Float, false, 8 * 4, 6 * 4));
        ctx.add(new EnableVertexAttribArray(2));

        return rv;
    }

    @Override
    public Command dispose() {
        return null;
    }

    @Override
    public Command draw() {
        if (displayList == null) {
            CommandList ctx = buildState();
            ctx.add(new BufferData(BufferType.ArrayBuffer, vertexBuffer.capacity() * 4, vertexBuffer, BufferData.Usage.StaticDraw));
            ctx.add(new BufferData(BufferType.ElementArrayBuffer, indicies.capacity() * 4, indicies, BufferData.Usage.StaticDraw));
            displayList = ctx;
        }

        return displayList;
    }

    @Override
    public void update(long t) {

    }

    public static Mesh create(Obj obj) {
        Mesh mesh = new Mesh();

        mesh.indicies = ObjData.getFaceNormalIndices(obj);

        int numVerticies = obj.getNumVertices();
        mesh.vertexBuffer = GLUtil.floatBuffer(numVerticies * 8);

        for(int i=0; i<numVerticies; i++) {
            Mesh.Vertex vert = new Mesh.Vertex();

            FloatTuple position = obj.getVertex(i);
            vert.position.set(position.getX(), position.getY(), position.getZ());
            vert.position.get(mesh.vertexBuffer);

            FloatTuple normal = obj.getNormal(i);
            vert.normal.set(normal.getX(), normal.getY(), normal.getZ());
            vert.normal.get(mesh.vertexBuffer);

            FloatTuple texCoord = obj.getTexCoord(i);
            vert.texCoords.set(texCoord.getX(), texCoord.getY());
            vert.texCoords.get(mesh.vertexBuffer);

            mesh.vertex.add(vert);
        }

        return mesh;
    }
}
