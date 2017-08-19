#version 330 core

out vec4 FragColor;
in vec3 vertexColor;

uniform sampler2D particleTexture;

void main()
{
    FragColor = texture(particleTexture, gl_PointCoord) * vec4(vertexColor, 1.0);
}