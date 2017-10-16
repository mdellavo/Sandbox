package org.quuux.opengl.renderer.states;

import org.quuux.opengl.renderer.Renderer;

public class Enable extends State {

    public enum Capability {
        DEPTH_TEST,
        BLEND,
        MULTISAMPLE,
        POINT_SIZE,
    }

    private final Capability capability;

    public Enable(Capability capability) {
        this.capability = capability;
    }

    public Capability getCapability() {
        return capability;
    }

    @Override
    void set(Renderer renderer) {
        renderer.set(this);
    }

    @Override
    void clear(Renderer renderer) {
        renderer.clear(this);
    }

}
