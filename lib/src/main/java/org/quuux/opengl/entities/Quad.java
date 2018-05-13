package org.quuux.opengl.entities;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.quuux.opengl.lib.BufferType;
import org.quuux.opengl.lib.ShaderProgram;
import org.quuux.opengl.lib.Texture2D;
import org.quuux.opengl.lib.VAO;
import org.quuux.opengl.lib.BufferObject;
import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.CommandList;
import org.quuux.opengl.renderer.commands.BufferData;
import org.quuux.opengl.renderer.commands.DrawArrays;
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
import org.quuux.opengl.scenes.Camera;
import org.quuux.opengl.util.GLUtil;
import org.quuux.opengl.util.ResourceUtil;

import java.nio.FloatBuffer;

public class Quad implements Entity {

    float vertices[] = {
            // verticies       // texture coords
             1,  1, 0,  1, 1,  // top right
             1, -1, 0,  1, 0,  // bottom right
            -1,  1, 0,  0, 1,  // top left

             1, -1, 0,  1, 0,  // bottom right
            -1, -1, 0,  0, 0,  // bottom left
            -1,  1, 0,  0, 1,  // top left
    };

    Matrix4d model = new Matrix4d().identity();
    Matrix4f mvp = new Matrix4f();
    FloatBuffer mvpBuffer = GLUtil.floatBuffer(16);

    BufferObject vbo = new BufferObject();
    VAO vao = new VAO();

    FloatBuffer vertexBuffer = GLUtil.floatBuffer(vertices);

    Texture2D texture = new Texture2D();
    ShaderProgram shader = new ShaderProgram();

    Command displayList;

    @Override
    public Command initialize() {
        CommandList rv = new CommandList();

        rv.add(ShaderProgram.build(shader, ResourceUtil.getStringResource("shaders/quad.vert.glsl"), ResourceUtil.getStringResource("shaders/quad.frag.glsl")));
        rv.add(new GenerateArray(vao));
        rv.add(new GenerateBuffer(vbo));

        BatchState ctx = new BatchState(new UseProgram(shader), new BindBuffer(BufferType.ArrayBuffer, vbo), new BindArray(vao));
        rv.add(ctx);

        ctx.add(new BufferData(BufferType.ArrayBuffer, vertices.length * 4, vertexBuffer, BufferData.Usage.StaticDraw));
        ctx.add(new VertexAttribPointer(0, 3, VertexAttribPointer.Type.Float, false, 5 * 4, 0));
        ctx.add(new EnableVertexAttribArray(0));
        ctx.add(new VertexAttribPointer(1, 2, VertexAttribPointer.Type.Float, false, 5 * 4, 3 * 4));
        ctx.add(new EnableVertexAttribArray(1));
        return rv;
    }

    @Override
    public Command dispose() {
        return null;
    }

    public Command buildDisplayList() {
        BatchState rv = new BatchState(new UseProgram(shader), new BindBuffer(BufferType.ArrayBuffer, vbo), new BindArray(vao), new BindTexture(texture), new ActivateTexture(0));
        rv.add(new SetUniformMatrix(shader, "mvp", 1, false, mvpBuffer));
        rv.add(new SetUniform(shader, "texture", 0));
        rv.add(new DrawArrays(DrawMode.Triangles, 0, 6));
        return rv;
    }

    @Override
    public Command draw() {
        Camera.getCamera().modelViewProjectionMatrix(model, mvp);
        mvp.get(mvpBuffer);

        if (displayList == null) {
            displayList = buildDisplayList();
        }

        return displayList;
    }

    @Override
    public void update(long t) {
        // noop
    }

    public Matrix4d getModel() {
        return model;
    }

    public void setTexture(Texture2D texture) {
        this.texture = texture;
    }

}
