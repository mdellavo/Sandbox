#version 330 core

out vec4 FragColor;
in vec4 vertexColor;

uniform sampler2D particleTexture;

void main()
{
    vec4 texColor = texture(particleTexture, gl_PointCoord) * vertexColor;
    if (texColor.a < .1)
        discard;
    FragColor = texColor;
}