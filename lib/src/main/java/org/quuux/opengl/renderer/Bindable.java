package org.quuux.opengl.renderer;

import com.jogamp.opengl.GL;

public interface Bindable {
     void bind(GL gl);
     void clear(GL gl);
}
