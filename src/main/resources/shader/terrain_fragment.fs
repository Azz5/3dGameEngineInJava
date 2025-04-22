#version 400 core

const int MAX_POINT_LIGHT = 5;
const int MAX_SPOT_LIGHT = 5;

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

struct PointLight {
    vec3 colour;
    vec3 position;
    float intensity;
    float constant;
    float linear;
    float exponent;
};

struct SpotLight {
    PointLight pl;
    vec3 conedir;
    float cutoff;
};

uniform sampler2D backgroundTexture;
uniform sampler2D redTexture;
uniform sampler2D greenTexture;
uniform sampler2D blueTexture;
uniform sampler2D blendMap;

uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHT];
uniform SpotLight spotLights[MAX_SPOT_LIGHT];

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColour(Material material, vec2 textCoord) {
    if (material.hasTexture == 0) {

        vec4 blendMapColour = texture(blendMap, textCoord);
        float backgroundTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
        vec2 tiledCoord = textCoord / 2.5f;
        vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoord) * backgroundTextureAmount;
        vec4 redTextureColour = texture(redTexture, tiledCoord) * blendMapColour.r;
        vec4 greenTextureColour = texture(greenTexture, tiledCoord) * blendMapColour.g;
        vec4 blueTextureColour = texture(blueTexture, tiledCoord) * blendMapColour.b;

        ambientC = backgroundTextureColour + redTextureColour + greenTextureColour + blueTextureColour ;
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
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.position - position;
    vec3 light_to_dir = normalize(light_dir);
    vec4 light_colour =calcLightColour(light.colour,light.intensity,position,light_to_dir,normal);

    //attenuation

    float distance = length(light_dir);
    float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
    return light_colour / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.pl.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec3 from_light_dir = -to_light_dir;

    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 colour = vec4(0,0,0,0);

    if (spot_alfa > light.cutoff) {
        colour = calcPointLight(light.pl,position,normal);
        colour *= (1.0 - (1.0 - spot_alfa) / (1.0 -  light.cutoff));
    }

    return colour;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColour(light.colour, light.intensity,position,normalize(light.direction),normal);
}
void main() {
    setupColour(material, fragTextureCoord);

    vec4 lightComp   = calcDirectionalLight(directionalLight, fragPos, fragNormal);

    for(int i = 0; i < MAX_POINT_LIGHT;i++) {
        if(pointLights[i].intensity > 0){
            lightComp += calcPointLight(pointLights[i],fragPos,fragNormal);
        }
    }
    for(int i = 0; i < MAX_SPOT_LIGHT;i++) {
        if (spotLights[i].pl.intensity > 0) {
            lightComp += calcSpotLight(spotLights[i],fragPos,fragNormal);
        }
    }
    // add all three contributions
    vec4 ambientComp = ambientC * vec4(ambientLight, 1.0);
    fragColour = ambientComp + lightComp;
}