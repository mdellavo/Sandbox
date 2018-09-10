package org.quuux.opengl.entities;

import org.quuux.opengl.lib.ArrayObject;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.Cubemap;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.DrawArrays;
import org.quuux.opengl.renderer.commands.DrawMode;
import org.quuux.opengl.renderer.commands.EnableVertexAttribArray;
import org.quuux.opengl.renderer.commands.GenerateArray;
import org.quuux.opengl.renderer.commands.GenerateBuffer;
import org.quuux.opengl.renderer.commands.VertexAttribPointer;
import org.quuux.opengl.renderer.states.BatchState;
import org.quuux.opengl.renderer.states.BindArray;
import org.quuux.opengl.renderer.states.BindBuffer;
import org.quuux.opengl.renderer.states.UseProgram;
import org.quuux.opengl.util.GLUtil;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;

public class Skybox implements Entity {

    float vertices[] = {
            // positions
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
    };

    final FloatBuffer vertexBuffer = GLUtil.floatBuffer(vertices);

    final String key;
    final Cubemap cubemap;

    ShaderProgram shader = new ShaderProgram();

    BufferObject vbo = new BufferObject();
    ArrayObject vao = new ArrayObject();

    public Skybox(String key) {
        this.key = key;
        cubemap = Cubemap.load(key);
    }

    CommandList buildState() {
        BatchState ctx = new BatchState(
                new UseProgram(shader),
                new BindArray(vao),
                new BindBuffer(BufferType.ArrayBuffer, vbo),
                cubemap.bind(0)
        );
        return ctx;
    }

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));

        rv.add(ShaderProgram.build(shader,
                ResourceUtil.getStringResource("shaders/skybox.vert.glsl"),
                ResourceUtil.getStringResource("shaders/skybox.frag.glsl")));

        rv.add(cubemap.initialize(0));
        CommandList ctx = buildState();
        rv.add(ctx);

        ctx.add(new VertexAttribPointer(0, 3, VertexAttribPointer.Type.Float, false, 3 * 4, 0));
        ctx.add(new EnableVertexAttribArray(0));
        ctx.add(new DrawArrays(DrawMode.Triangles, 0, vertices.length / 3));

        return rv;
    }

    @Override
    public Command dispose() {
        CommandList rv = new CommandList();
        return rv;
    }

    @Override
    public Command draw() {
        CommandList ctx = buildState();
        ctx.add(cubemap.bind(0));
        ctx.add(new BufferData(BufferType.ArrayBuffer, vertexBuffer.capacity() * 4, vertexBuffer, BufferData.Usage.StaticDraw));
        ctx.add(new DrawArrays(DrawMode.Triangles, 0, vertexBuffer.capacity() / 8));
        return ctx;
    }

    @Override
    public void update(final long t) {

    }

}
