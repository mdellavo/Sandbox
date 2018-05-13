out vec4 FragColor;

in vec3 Normal;
in vec2 TexCoords;

uniform sampler2D texture;

void main()
{

    //FragColor = texture(texture, TexCoords);
    FragColor = vec4(1, 1, 1, 1);
}