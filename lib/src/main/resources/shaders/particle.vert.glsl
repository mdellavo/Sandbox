layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in float aSize;

uniform mat4 mvp;

out vec4 vertexColor;

void main()
{
    gl_Position = mvp * vec4(aPos, 1.0);
    gl_PointSize = aSize;
    vertexColor = aColor;
}