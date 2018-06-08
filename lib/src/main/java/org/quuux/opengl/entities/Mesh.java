package org.quuux.opengl.entities;

import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
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
import org.quuux.opengl.renderer.commands.GenerateTexture2D;
import org.quuux.opengl.renderer.commands.LoadTexture2D;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.ActivateTexture;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.BindTexture;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.scenes.Camera;
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

    Texture2D diffuse = new Texture2D();
    Texture2D specular = new Texture2D();

    Material material = new Material(diffuse, specular, 32f);

    FloatBuffer vertexBuffer;

    BufferObject vbo = new BufferObject();
    BufferObject ebo = new BufferObject();
    VAO vao = new VAO();

    ShaderProgram shader = new ShaderProgram();

    Matrix4d model = new Matrix4d().identity();

    FloatBuffer modelBuffer = GLUtil.floatBuffer(16);
    FloatBuffer viewBuffer = GLUtil.floatBuffer(16);
    FloatBuffer projectionBuffer = GLUtil.floatBuffer(16);

    Command displayList;

    CommandList buildState() {
        BatchState rv = new BatchState(
                new UseProgram(shader),
                new BindArray(vao),
                new BindBuffer(BufferType.ArrayBuffer, vbo),
                new BindBuffer(BufferType.ElementArrayBuffer, ebo),
                new BindTexture(diffuse),
                new ActivateTexture(0),
                new BindTexture(specular),
                new ActivateTexture(1)
        );
        return rv;
    }

    @Override
    public Command initialize() {
        model.scale(5);

        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateBuffer(ebo));

        rv.add(new GenerateTexture2D(diffuse));

        BatchState tex1State = new BatchState(new BindTexture(diffuse), new ActivateTexture(0));
        rv.add(tex1State);

        ResourceUtil.DecodedImage diffuseImage = ResourceUtil.getPNGResource("textures/world-diffuse.png");
        tex1State.add(new LoadTexture2D(diffuse, LoadTexture2D.Format.RGBA, diffuseImage.width, diffuseImage.height,  LoadTexture2D.Format.RGBA, diffuseImage.buffer, LoadTexture2D.Filter.LINEAR, LoadTexture2D.Filter.LINEAR));

        rv.add(new GenerateTexture2D(specular));
        BatchState tex2State = new BatchState(new BindTexture(specular), new ActivateTexture(1));
        rv.add(tex2State);
        ResourceUtil.DecodedImage specularImage = ResourceUtil.getPNGResource("textures/world-diffuse.png");
        tex2State.add(new LoadTexture2D(specular, LoadTexture2D.Format.RGBA, specularImage.width, specularImage.height,  LoadTexture2D.Format.RGBA, specularImage.buffer, LoadTexture2D.Filter.LINEAR, LoadTexture2D.Filter.LINEAR));

        rv.add(ShaderProgram.build(shader,
                ResourceUtil.getStringResource("shaders/mesh.vert.glsl"),
                ResourceUtil.getStringResource("shaders/mesh.frag.glsl")));

        CommandList ctx = buildState();
        rv.add(ctx);

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
            ctx.add(new SetUniformMatrix(shader, "model", 1, false, modelBuffer));
            ctx.add(new SetUniformMatrix(shader, "view", 1, false, viewBuffer));
            ctx.add(new SetUniformMatrix(shader, "projection", 1, false, projectionBuffer));

            Vector3d viewPos =  Scene.get().camera.eye;
            ctx.add(new SetUniform(shader, "viewPos", (float)viewPos.x, (float)viewPos.y, (float)viewPos.z));

            ctx.add(new SetUniform(shader, "material.diffuse", 0));
            ctx.add(new SetUniform(shader, "material.specular", 1));
            ctx.add(new SetUniform(shader, "material.shininess", material.shininess));

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
                ctx.add(new SetUniform(shader, key + ".position", pointLight.position));
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
        Camera camera = Scene.get().getCamera();
        model.get(modelBuffer);
        camera.viewMatrix.get(viewBuffer);
        camera.projectionMatrix.get(projectionBuffer);
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
