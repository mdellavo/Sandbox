package org.quuux.opengl.renderer.states;

import com.jogamp.opengl.GL;

import org.quuux.opengl.renderer.Bindable;
import org.quuux.opengl.renderer.State;

public class Bind extends State {
    private final Bindable binding;

    public Bind(Bindable binding) {
        this.binding = binding;
    }

    @Override
    public void clearState(GL gl) {
        binding.clear(gl);
    }

    @Override
    public void setState(GL gl) {
        binding.bind(gl);
    }
}
