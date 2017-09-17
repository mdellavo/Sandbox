#version 330 core

in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D texture;

void main()
{
 const float gamma = 1.8;
    vec3 hdrColor = texture(texture, TexCoords).rgb;

    // reinhard tone mapping
    vec3 mapped = hdrColor / (hdrColor + vec3(1.0));
    // gamma correction
    mapped = pow(mapped, vec3(1.0 / gamma));
    FragColor = vec4(mapped, 1.0);
}