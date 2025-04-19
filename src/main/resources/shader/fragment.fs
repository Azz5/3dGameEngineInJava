#version 400 core

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColour;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 colour;
    vec3 direction;
    float intensity;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColour(Material material, vec2 textCoord) {
    if (material.hasTexture == 1) {
        ambientC = texture(textureSampler,fragTextureCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}
vec4 calcLightColour(
    vec3 light_color,
    float light_intensity,
    vec3 position,
    vec3 to_light_dir,
    vec3 normal
) {
    // start from zero
    vec4 diffuseColour  = vec4(0.0);
    vec4 specularColour = vec4(0.0);

    // –– diffuse term
    float diffFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC                              // the colour you set
                  * diffFactor                          // lambert
                  * light_intensity                     // intensity
                  * vec4(light_color, 1.0);            // light colour

    // –– specular term (unchanged)
    vec3 viewDir      = normalize(-position);
    vec3 reflectDir   = reflect(-to_light_dir, normal);
    float specFactor  = pow(max(dot(viewDir, reflectDir), 0.0), specularPower);
    specularColour    = specularC
                     * specFactor
                     * material.reflectance
                     * vec4(light_color, 1.0);

    return diffuseColour + specularColour;
}vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColour(light.colour, light.intensity,position,normalize(light.direction),normal);
}
void main() {
    setupColour(material, fragTextureCoord);

    vec4 lightComp   = calcDirectionalLight(directionalLight, fragPos, fragNormal);
    vec4 ambientComp = ambientC * vec4(ambientLight, 1.0);

    // add all three contributions
    fragColour = ambientComp + lightComp;
}