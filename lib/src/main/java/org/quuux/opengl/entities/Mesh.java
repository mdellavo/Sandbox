package org.quuux.opengl.entities;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.DrawElements;
import org.quuux.opengl.renderer.commands.DrawMode;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.scenes.DirectionalLight;
import org.quuux.opengl.scenes.PointLight;
import org.quuux.opengl.scenes.Scene;
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

    List<Texture> textures = new ArrayList<>();
    Material material;

    FloatBuffer vertexBuffer;

    BufferObject vbo = new BufferObject();
    BufferObject ebo = new BufferObject();
    VAO vao = new VAO();

    ShaderProgram shader = new ShaderProgram();

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLUtil.floatBuffer(16);

    Command displayList;

    CommandList buildState() {
        BatchState rv = new BatchState(
                new UseProgram(shader),
                new BindArray(vao),
                new BindBuffer(BufferType.ArrayBuffer, vbo),
                new BindBuffer(BufferType.ElementArrayBuffer, ebo)
        );
        for (int i=0; i<textures.size(); i++) {
            rv.add(new BindTexture(textures.get(i)));
            rv.add(new ActivateTexture(i));
        }
        return rv;
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateBuffer(ebo));

        rv.add(ShaderProgram.build(shader,
                ResourceUtil.getStringResource("shaders/mesh.vert.glsl"),
                ResourceUtil.getStringResource("shaders/mesh.frag.glsl")));

        CommandList ctx = buildState();
        rv.add(ctx);

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
            ctx.add(new SetUniformMatrix(shader, "mvp", 1, false, mvpBuffer));

            if (material != null) {
                ctx.add(new SetUniform(shader, "material.ambient", material.ambient));
                ctx.add(new SetUniform(shader, "material.diffuse", material.diffuse));
                ctx.add(new SetUniform(shader, "material.specular", material.specular));
                ctx.add(new SetUniform(shader, "material.shininess", material.shininess));
            }

            Scene scene = Scene.get();
            DirectionalLight directionalLight = scene.directionalLight;
            if (directionalLight != null) {
                ctx.add(new SetUniform(shader, "dirLight.direction", directionalLight.direction));
                ctx.add(new SetUniform(shader, "dirLight.ambient", directionalLight.ambient));
                ctx.add(new SetUniform(shader, "dirLight.diffuse", directionalLight.diffuse));
                ctx.add(new SetUniform(shader, "dirLight.specular", directionalLight.specular));
            }
            
            for (int i=0; i<scene.pointLights.size(); i++) {
                PointLight pointLight = scene.pointLights.get(i);
                String key = String.format("pointLights[%d]", i);
                ctx.add(new SetUniform(shader, key + ".direction", pointLight.position));
                ctx.add(new SetUniform(shader, key + ".constant", pointLight.constant));
                ctx.add(new SetUniform(shader, key + ".linear", pointLight.linear));
                ctx.add(new SetUniform(shader, key + ".quadratic", pointLight.quadratic));
                ctx.add(new SetUniform(shader, key + ".ambient", pointLight.ambient));
                ctx.add(new SetUniform(shader, key + ".diffuse", pointLight.diffuse));
                ctx.add(new SetUniform(shader, key + ".specular", pointLight.specular));
            }

            ctx.add(new BufferData(BufferType.ArrayBuffer, vertexBuffer.capacity() * 4, vertexBuffer, BufferData.Usage.StaticDraw));
            ctx.add(new BufferData(BufferType.ElementArrayBuffer, indicies.capacity() * 4, indicies, BufferData.Usage.StaticDraw));
            ctx.add(new DrawElements(DrawMode.Triangles, vertex.size()));
            displayList = ctx;
        }

        return displayList;
    }

    @Override
    public void update(long t) {
        Scene.get().getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);
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
            mesh.vertexBuffer.position(mesh.vertexBuffer.position() + 3);

            FloatTuple normal = obj.getNormal(i);
            vert.normal.set(normal.getX(), normal.getY(), normal.getZ());
            vert.normal.get(mesh.vertexBuffer);
            mesh.vertexBuffer.position(mesh.vertexBuffer.position() + 3);

            FloatTuple texCoord = obj.getTexCoord(i);
            vert.texCoords.set(texCoord.getX(), texCoord.getY());
            vert.texCoords.get(mesh.vertexBuffer);
            mesh.vertexBuffer.position(mesh.vertexBuffer.position() + 2);

            mesh.vertex.add(vert);
        }

        mesh.vertexBuffer.position(0);

        System.out.println("num verticies = " + numVerticies + " / buffer = " + mesh.vertexBuffer.limit() + " / indicies = " + mesh.indicies.limit());

        return mesh;
    }
}
