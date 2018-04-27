out vec4 FragColor;
in vec4 vertexColor;

uniform sampler2D texture;

void main()
{
    vec4 texColor = texture(texture, gl_PointCoord) * vertexColor;
    if (texColor.a < .01)
        discard;
    FragColor = texColor;
}