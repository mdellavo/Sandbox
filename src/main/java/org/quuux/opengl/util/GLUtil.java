package org.quuux.opengl.util;

import com.jogamp.opengl.GL;

public class GLUtil {

    public static String getErrorMessage(GL gl) {
        int code = gl.glGetError();
        String rv = null;
        switch(code) {
            case GL.GL_NO_ERROR:
                break;

            case GL.GL_INVALID_ENUM:
                rv = "Invalid Enum";
                break;

            case GL.GL_INVALID_VALUE:
                rv = "Invalid Value";
                break;

            case GL.GL_INVALID_OPERATION:
                rv = "Invalid Operation";
                break;

            case GL.GL_INVALID_FRAMEBUFFER_OPERATION:
                rv = "Invalid Framebuffer Operation";
                break;

            default:
                rv = "Unknown Error";
                break;
        }
        return rv;
    }

    public static boolean checkError(GL gl){
        String error = getErrorMessage(gl);
        if (error != null) {
            System.out.println("Error: " + error);
        }
        return error != null;
    }
}
