#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;  // world‑space normal
out vec3 fragPos;     // world‑space position

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    // 1) Compute world position
    vec4 worldPos4 = transformationMatrix * vec4(position, 1.0);
    fragPos        = worldPos4.xyz;

    // 2) Transform normal by the model's upper‑left 3×3
    //    (assuming no non‑uniform scale, this is OK)
    fragNormal = normalize(mat3(transformationMatrix) * normal);

    // 3) Standard projection
    gl_Position = projectionMatrix * viewMatrix * worldPos4;

    fragTextureCoord = textureCoord;
}
