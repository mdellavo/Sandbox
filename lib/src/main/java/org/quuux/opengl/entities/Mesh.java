package org.quuux.opengl.entities;

import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.Material;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.DrawElements;
import org.quuux.opengl.renderer.commands.DrawMode;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.SetUniform;
import org.quuux.opengl.renderer.commands.SetUniformMatrix;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;


public class Mesh implements Entity {

    public Material material;

    int numVerticies;
    IntBuffer indicies;
    FloatBuffer vertexBuffer;

    BufferObject vbo = new BufferObject();
    BufferObject ebo = new BufferObject();
    VAO vao = new VAO();

    ShaderProgram shader = new ShaderProgram();

    public Matrix4d model = new Matrix4d().identity();

    FloatBuffer modelBuffer = GLUtil.floatBuffer(16);
    FloatBuffer viewBuffer = GLUtil.floatBuffer(16);
    FloatBuffer projectionBuffer = GLUtil.floatBuffer(16);

    Command displayList;


    protected Mesh(Material material) {
        this.material = material;
    }

    CommandList buildState() {
        BatchState rv = new BatchState(
                new UseProgram(shader),
                new BindArray(vao),
                new BindBuffer(BufferType.ArrayBuffer, vbo),
                new BindBuffer(BufferType.ElementArrayBuffer, ebo),
                material.bind()
        );
        return rv;
    }

    @Override
    public Command initialize() {

        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));
        rv.add(new GenerateBuffer(ebo));

        rv.add(material.initialize());

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

            Vector3d viewPos = Scene.get().camera.position;
            ctx.add(new SetUniform(shader, "viewPos", (float) viewPos.x, (float) viewPos.y, (float) viewPos.z));

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

            for (int i = 0; i < scene.pointLights.size(); i++) {
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

            if (indicies != null) {
                ctx.add(new BufferData(BufferType.ElementArrayBuffer, indicies.capacity() * 4, indicies, BufferData.Usage.StaticDraw));
                ctx.add(new DrawElements(DrawMode.Triangles, indicies.capacity()));
            } else {
                ctx.add(new DrawArrays(DrawMode.Triangles, 0, numVerticies));
            }

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

    public static Mesh fromObj(Material material, Obj obj) {
        Mesh mesh = new Mesh(material);

        mesh.indicies = ObjData.getFaceNormalIndices(obj);
        mesh.numVerticies = obj.getNumVertices();
        mesh.vertexBuffer = GLUtil.floatBuffer(mesh.numVerticies * 8);

        for (int i = 0; i < mesh.numVerticies; i++) {
            FloatTuple position = obj.getVertex(i);
            mesh.vertexBuffer.put(position.getX());
            mesh.vertexBuffer.put(position.getY());
            mesh.vertexBuffer.put(position.getZ());

            FloatTuple normal = obj.getNormal(i);
            mesh.vertexBuffer.put(normal.getX());
            mesh.vertexBuffer.put(normal.getY());
            mesh.vertexBuffer.put(normal.getZ());

            FloatTuple texCoord = obj.getTexCoord(i);
            mesh.vertexBuffer.put(texCoord.getX());
            mesh.vertexBuffer.put(texCoord.getY());
        }

        mesh.vertexBuffer.position(0);

        System.out.println("num verticies = " + mesh.numVerticies + " / buffer = " + mesh.vertexBuffer.limit() + " / indicies = " + mesh.indicies.limit());

        return mesh;
    }

    public static Mesh createUVSphere(Material material, double radius, int rings, int sectors) {

        final double R = 1. / (double) (rings - 1);
        final double S = 1. / (double) (sectors - 1);

        Mesh mesh = new Mesh(material);
        mesh.numVerticies = rings * sectors * 3;
        mesh.vertexBuffer = GLUtil.floatBuffer(mesh.numVerticies * 8);
        mesh.indicies = GLUtil.intBuffer(mesh.numVerticies * 3);

        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {
                double y = Math.sin(-(Math.PI / 2) + Math.PI * r * R);
                double x = Math.cos(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R);
                double z = Math.sin(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R);

                mesh.vertexBuffer.put((float) (x * radius));
                mesh.vertexBuffer.put((float) (y * radius));
                mesh.vertexBuffer.put((float) (z * radius));

                mesh.vertexBuffer.put((float) x);
                mesh.vertexBuffer.put((float) y);
                mesh.vertexBuffer.put((float) z);

                mesh.vertexBuffer.put((float) (s * S));
                mesh.vertexBuffer.put((float) (r * R));
            }
        }

        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {

                mesh.indicies.put(r * sectors + s);
                mesh.indicies.put(r * sectors + (s + 1));
                mesh.indicies.put((r + 1) * sectors + (s + 1));

                mesh.indicies.put((r + 1) * sectors + (s + 1));
                mesh.indicies.put((r + 1) * sectors + s);
                mesh.indicies.put(r * sectors + s);
            }
        }

        mesh.vertexBuffer.position(0);
        mesh.indicies.position(0);

        return mesh;
    }

    static class TriangleIndices {
        int v1, v2, v3;

        public TriangleIndices(final int v1, final int v2, final int v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }

    static class VertexSet {
        List<Vector3d> verts = new ArrayList<>();

        int add(Vector3d vert) {
            vert.normalize();
            verts.add(vert);
            return verts.size() - 1;
        }

        void addAll(Vector3d[] verts) {
            for(Vector3d vert : verts) {
                add(vert);
            }
        }
    }

    static class VertexKey {
        final int a, b;

        VertexKey(int a, int b) {
            this.a = Math.min(a, b);
            this.b = Math.max(a, b);
        }

        @Override
        public boolean equals(final Object o) {
            return o instanceof VertexKey && ((VertexKey)o).a == a && ((VertexKey)o).b == b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    // return index of point in the middle of p1 and p2
    static int getMiddlePoint(Map<VertexKey, Integer> cache, VertexSet verts, int a, int b) {

        VertexKey key = new VertexKey(a, b);
        if (cache.containsKey(key)) {
           return cache.get(key);
        }

        Vector3d p1 = verts.verts.get(a);
        Vector3d p2 = verts.verts.get(b);

        // not in cache, calculate it
        Vector3d middle = new Vector3d(
                (p1.x + p2.x) / 2.0,
                (p1.y + p2.y) / 2.0,
                (p1.z + p2.z) / 2.0);

        int index = verts.add(middle);
        cache.put(key, index);

        return index;
    }


    // http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
    public static Mesh createIcoSphere(Material material, double radius, int recursionLevel) {
        Mesh mesh = new Mesh(material);
        VertexSet vertexSet = new VertexSet();
        List<TriangleIndices> faces = new ArrayList<>();
        Map<VertexKey, Integer> cache = new HashMap<>();

        double t = (1.0 + Math.sqrt(5.0)) / 2.0;
        Vector3d verts[] = {
                new Vector3d(-1, t, 0),
                new Vector3d(1, t, 0),
                new Vector3d(-1, -t, 0),
                new Vector3d(1, -t, 0),

                new Vector3d(0, -1, t),
                new Vector3d(0, 1, t),
                new Vector3d(0, -1, -t),
                new Vector3d(0, 1, -t),

                new Vector3d(t, 0, -1),
                new Vector3d(t, 0, 1),
                new Vector3d(-t, 0, -1),
                new Vector3d(-t, 0, 1),
        };
        vertexSet.addAll(verts);

        faces.add(new TriangleIndices(0, 11, 5));
        faces.add(new TriangleIndices(0, 5, 1));
        faces.add(new TriangleIndices(0, 1, 7));
        faces.add(new TriangleIndices(0, 7, 10));
        faces.add(new TriangleIndices(0, 10, 11));

        faces.add(new TriangleIndices(1, 5, 9));
        faces.add(new TriangleIndices(5, 11, 4));
        faces.add(new TriangleIndices(11, 10, 2));
        faces.add(new TriangleIndices(10, 7, 6));
        faces.add(new TriangleIndices(7, 1, 8));

        faces.add(new TriangleIndices(3, 9, 4));
        faces.add(new TriangleIndices(3, 4, 2));
        faces.add(new TriangleIndices(3, 2, 6));
        faces.add(new TriangleIndices(3, 6, 8));
        faces.add(new TriangleIndices(3, 8, 9));

        faces.add(new TriangleIndices(4, 9, 5));
        faces.add(new TriangleIndices(2, 4, 11));
        faces.add(new TriangleIndices(6, 2, 10));
        faces.add(new TriangleIndices(8, 6, 7));
        faces.add(new TriangleIndices(9, 8, 1));

        for (int i=0; i < recursionLevel; i++)  {
            List<TriangleIndices> faces2 = new ArrayList<>();
            for(TriangleIndices tri : faces) {
                int a = getMiddlePoint(cache, vertexSet, tri.v1, tri.v2);
                int b = getMiddlePoint(cache, vertexSet, tri.v2, tri.v3);
                int c = getMiddlePoint(cache, vertexSet, tri.v3, tri.v1);

                faces2.add(new TriangleIndices(tri.v1, a, c));
                faces2.add(new TriangleIndices(tri.v2, b, a));
                faces2.add(new TriangleIndices(tri.v3, c, b));
                faces2.add(new TriangleIndices(a, b, c));
            }
            faces = faces2;
        }

        mesh.numVerticies = vertexSet.verts.size();
        mesh.vertexBuffer = GLUtil.floatBuffer(mesh.numVerticies * 8);
        mesh.indicies = GLUtil.intBuffer(faces.size() * 3);

        for (Vector3d vert : vertexSet.verts) {
            mesh.vertexBuffer.put((float) (vert.x * radius));
            mesh.vertexBuffer.put((float) (vert.y * radius));
            mesh.vertexBuffer.put((float) (vert.z * radius));

            mesh.vertexBuffer.put((float)vert.x);
            mesh.vertexBuffer.put((float)vert.y);
            mesh.vertexBuffer.put((float)vert.z);

            double theta = (Math.atan2(vert.x, vert.z) / Math.PI) / 2.f + 0.5f;
            double phi = (Math.asin(-vert.y) / (Math.PI / 2.f)) / 2.f + 0.5f;
            mesh.vertexBuffer.put((float) theta);
            mesh.vertexBuffer.put((float) phi);
        }

        for (TriangleIndices face : faces) {
            mesh.indicies.put(face.v1);
            mesh.indicies.put(face.v2);
            mesh.indicies.put(face.v3);
        }

        mesh.vertexBuffer.position(0);
        mesh.indicies.position(0);

        System.out.println(String.format("num verts = %s / num faces = %s", mesh.vertexBuffer.capacity(), mesh.indicies.capacity()));

        return mesh;
    }

    public static Mesh create(Material material, float[] vertices) {
        Mesh mesh = new Mesh(material);
        mesh.numVerticies = vertices.length / 8;
        mesh.vertexBuffer = GLUtil.floatBuffer(vertices.length);
        mesh.vertexBuffer.put(vertices);
        mesh.vertexBuffer.position(0);
        return mesh;
    }

    public static Mesh createQuad(Material material) {
        float vertices[] = {
                // vert    normal   texture-coords
                 .5f,  .5f, 0, 0, 0, 1, 1, 1,  // top right
                 .5f, -.5f, 0, 0, 0, 1, 1, 0,  // bottom right
                -.5f,  .5f, 0, 0, 0, 1, 0, 1,  // top left

                 .5f, -.5f, 0, 0, 0, 1, 1, 0,  // bottom right
                -.5f, -.5f, 0, 0, 0, 1, 0, 0,  // bottom left
                -.5f,  .5f, 0, 0, 0, 1, 0, 1,  // top left
        };

        return create(material, vertices);
    }

    public static Mesh createCube(Material material) {
        float vertices[] = {
                // positions          normals             texture coords
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f
        };
        return create(material, vertices);
    }
}