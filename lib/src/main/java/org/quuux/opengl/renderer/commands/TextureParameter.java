package org.quuux.opengl.renderer.commands;

import org.quuux.opengl.renderer.Command;
import org.quuux.opengl.renderer.Renderer;
import org.quuux.opengl.renderer.states.TextureTarget;

public class TextureParameter extends Command {

    public enum Parameter {
        MIN_FILTER,
        MAG_FILTER,
        WRAP_S,
        WRAP_T,
    }

    public enum Filter {
        NEAREST,
        LINEAR,
        LINEAR_MIPMAP_LINEAR,
    }

    public enum Wrap {
        CLAMP,
        REPEAT,
    }

    private final TextureTarget target;
    private final Parameter parameter;
    private final Object value;

    public TextureParameter(TextureTarget target, Parameter parameter, Filter value) {
        this.target = target;
        this.parameter = parameter;
        this.value = value;
    }

    public TextureParameter(TextureTarget target, Parameter parameter, Wrap value) {
        this.target = target;
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    public void run(final Renderer renderer) {
        renderer.run(this);
    }

    public TextureTarget getTarget() {
        return target;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Filter getFilter() {
        return (Filter) value;
    }

    public Wrap getWrap() {
        return (Wrap) value;
    }
}
