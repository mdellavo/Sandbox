package org.quuux.sandbox;

import android.os.Bundle;


import jogamp.newt.driver.android.NewtBaseActivity;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

public class SandboxActivity extends NewtBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GLCapabilities caps =
                new GLCapabilities(GLProfile.get(GLProfile.GLES2));
        final GLWindow glWindow = GLWindow.create(caps);
        glWindow.setFullscreen(true);

        this.setContentView(this.getWindow(), glWindow);

    }
}
