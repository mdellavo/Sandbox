package org.quuux.opengl.entities;

import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Material {
    String name;
    Vector3f ambient, diffuse, specular;
    float shininess;

    public Material(String name, Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.name = name;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Material(String name,
                    float ambientX, float ambientY, float ambientZ,
                    float diffuseX, float diffuseY, float diffuseZ,
                    float specularX, float specularY, float specularZ,
                    float shininess) {
        this(name, new Vector3f(ambientX, ambientY, ambientZ), new Vector3f(diffuseX, diffuseY, diffuseZ), new Vector3f(specularX, specularY, specularZ), shininess);
    }

    public static final String EMERALD = "emerald";
    public static final String JADE = "jade";
    public static final String OBSIDIAN = "obsidian";
    public static final String PEARL = "pearl";
    public static final String RUBY = "ruby";
    public static final String TURQUOISE = "turquoise";
    public static final String BRASS = "brass";
    public static final String BRONZE = "bronze";
    public static final String CHROME = "chrome";
    public static final String COPPER = "copper";
    public static final String GOLD = "gold";
    public static final String SILVER = "silver";
    public static final String BLACK_PLASTIC = "black plastic";
    public static final String CYAN_PLASTIC = "cyan plastic";
    public static final String GREEN_PLASTIC = "green plastic";
    public static final String RED_PLASTIC = "red plastic";
    public static final String WHITE_PLASTIC = "white plastic";
    public static final String YELLOW_PLASTIC = "yellow plastic";
    public static final String BLACK_RUBBER = "black rubber";
    public static final String CYAN_RUBBER = "cyan rubber";
    public static final String GREEN_RUBBER = "green rubber";
    public static final String RED_RUBBER = "red rubber";
    public static final String WHITE_RUBBER = "white rubber";
    public static final String YELLOW_RUBBER = "yellow rubber";
    
    static Material ALL_MATERIALS[] = {
            new Material(EMERALD, 0.0215f, 0.1745f, 0.0215f, 0.07568f, 0.61424f, 0.07568f, 0.633f, 0.727811f, 0.633f, 0.6f),
            new Material(JADE, 0.135f, 0.2225f, 0.1575f, 0.54f, 0.89f, 0.63f, 0.316228f, 0.316228f, 0.316228f, 0.1f),
            new Material(OBSIDIAN, 0.05375f, 0.05f, 0.06625f, 0.18275f, 0.17f, 0.22525f, 0.332741f, 0.328634f, 0.346435f, 0.3f),
            new Material(PEARL, 0.25f, 0.20725f, 0.20725f, 1f, 0.829f, 0.829f, 0.296648f, 0.296648f, 0.296648f, 0.088f),
            new Material(RUBY, 0.1745f, 0.01175f, 0.01175f, 0.61424f, 0.04136f, 0.04136f, 0.727811f, 0.626959f, 0.626959f, 0.6f),
            new Material(TURQUOISE, 0.1f, 0.18725f, 0.1745f, 0.396f, 0.74151f, 0.69102f, 0.297254f, 0.30829f, 0.306678f, 0.1f),
            new Material(BRASS, 0.329412f, 0.223529f, 0.027451f, 0.780392f, 0.568627f, 0.113725f, 0.992157f, 0.941176f, 0.807843f, 0.21794872f),
            new Material(BRONZE, 0.2125f, 0.1275f, 0.054f, 0.714f, 0.4284f, 0.18144f, 0.393548f, 0.271906f, 0.166721f, 0.2f),
            new Material(CHROME, 0.25f, 0.25f, 0.25f, 0.4f, 0.4f, 0.4f, 0.774597f, 0.774597f, 0.774597f, 0.6f),
            new Material(COPPER, 0.19125f, 0.0735f, 0.0225f, 0.7038f, 0.27048f, 0.0828f, 0.256777f, 0.137622f, 0.086014f, 0.1f),
            new Material(GOLD, 0.24725f, 0.1995f, 0.0745f, 0.75164f, 0.60648f, 0.22648f, 0.628281f, 0.555802f, 0.366065f, 0.4f),
            new Material(SILVER, 0.19225f, 0.19225f, 0.19225f, 0.50754f, 0.50754f, 0.50754f, 0.508273f, 0.508273f, 0.508273f, 0.4f),
            new Material(BLACK_PLASTIC, 0.0f, 0.0f, 0.0f, 0.01f, 0.01f, 0.01f, 0.50f, 0.50f, 0.50f, .25f),
            new Material(CYAN_PLASTIC, 0.0f, 0.1f, 0.06f, 0.0f, 0.50980392f, 0.50980392f, 0.50196078f, 0.50196078f, 0.50196078f, .25f),
            new Material(GREEN_PLASTIC, 0.0f, 0.0f, 0.0f, 0.1f, 0.35f, 0.1f, 0.45f, 0.55f, 0.45f, .25f),
            new Material(RED_PLASTIC, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.7f, 0.6f, 0.6f, .25f),
            new Material(WHITE_PLASTIC, 0.0f, 0.0f, 0.0f, 0.55f, 0.55f, 0.55f, 0.70f, 0.70f, 0.70f, .25f),
            new Material(YELLOW_PLASTIC, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.60f, 0.60f, 0.50f, .25f),
            new Material(BLACK_RUBBER, 0.02f, 0.02f, 0.02f, 0.01f, 0.01f, 0.01f, 0.4f, 0.4f, 0.4f, .078125f),
            new Material(CYAN_RUBBER, 0.0f, 0.05f, 0.05f, 0.4f, 0.5f, 0.5f, 0.04f, 0.7f, 0.7f, .078125f),
            new Material(GREEN_RUBBER, 0.0f, 0.05f, 0.0f, 0.4f, 0.5f, 0.4f, 0.04f, 0.7f, 0.04f, .078125f),
            new Material(RED_RUBBER, 0.05f, 0.0f, 0.0f, 0.5f, 0.4f, 0.4f, 0.7f, 0.04f, 0.04f, .078125f),
            new Material(WHITE_RUBBER, 0.05f, 0.05f, 0.05f, 0.5f, 0.5f, 0.5f, 0.7f, 0.7f, 0.7f, .078125f),
            new Material(YELLOW_RUBBER, 0.05f, 0.05f, 0.0f, 0.5f, 0.5f, 0.4f, 0.7f, 0.7f, 0.04f, .078125f),
    };

    static Map<String, Material> MATERIALS = new HashMap<>();
    static {
        for(Material material : ALL_MATERIALS)
            MATERIALS.put(material.name, material);
    }

    public static Material getMaterial(String name) {
        return MATERIALS.get(name);
    }
}
