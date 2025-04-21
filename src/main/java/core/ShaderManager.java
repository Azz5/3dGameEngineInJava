package core;

import core.entity.Material;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.lighting.SpotLight;
import core.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    private final int PROGRAMID;

    private int vertexShaderId, fragmentShaderId;

    private final Map<String , Integer> uniforms;

    public ShaderManager() throws Exception {
        PROGRAMID = GL20.glCreateProgram();

        if (PROGRAMID == 0) {
            throw new Exception("Could not create shader");
        }

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception {
        System.out.println(uniformName);
        int uniformLocation = GL20.glGetUniformLocation(PROGRAMID,uniformName);
        if (uniformLocation < 0) {

/* Only use if there is error in shaders
            int uniformCount = GL20.glGetProgrami(PROGRAMID, GL20.GL_ACTIVE_UNIFORMS);

            int maxNameLength = GL20.glGetProgrami(PROGRAMID, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);

            IntBuffer sizeBuf = BufferUtils.createIntBuffer(1);
            IntBuffer typeBuf = BufferUtils.createIntBuffer(1);

            System.out.printf("There are %d active uniforms, max name length %d%n",
                    uniformCount, maxNameLength);

            for (int i = 0; i < uniformCount; i++) {
                // this overload pulls back name, size & type in one go
                String name = GL20.glGetActiveUniform(PROGRAMID, i, maxNameLength, sizeBuf, typeBuf);


                int size = sizeBuf.get(0);
                int type = typeBuf.get(0);

                System.out.printf("Uniform #%d → name=\"%s\", size=%d, type=0x%X%n",
                        i, name, size, type);
                sizeBuf.rewind();
                typeBuf.rewind();
            }

            */



            throw new Exception("Could not find uniform " + uniformName);
        }
        uniforms.put(uniformName,uniformLocation);
    }

    public void createDirectionalLightUnifrom(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
    }

    public void createSpotLightUniform(String prefix) throws Exception {
        // Register nested PointLight fields under SpotLight
        createUniform(prefix + ".pl.colour");
        createUniform(prefix + ".pl.position");
        createUniform(prefix + ".pl.intensity");
        createUniform(prefix + ".pl.constant");
        createUniform(prefix + ".pl.linear");
        createUniform(prefix + ".pl.exponent");
        // Register SpotLight-specific fields
        createUniform(prefix + ".conedir");
        createUniform(prefix + ".cutoff");
    }

    public void createPointLightListUniform(String uniformName, int size) throws  Exception{
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName+"["+i+"]");
        }
    }
    public void createSpotLightListUniform(String uniformName, int size) throws  Exception{
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName+"["+i+"]");
        }
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName+".ambient");
        createUniform(uniformName+".diffuse");
        createUniform(uniformName+".specular");
        createUniform(uniformName+".hasTexture");
        createUniform(uniformName+".reflectance");
    }


    public void setUniforms(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(uniforms.get(uniformName),false,value.get(stack.mallocFloat(16)));
        }
    }
    public void setUniforms(String uniformName, Vector4f value) {
        GL20.glUniform4f(uniforms.get(uniformName),value.x,value.y,value.z, value.w);
    }

    public void setUniforms(String uniformName, Vector3f value) {
        GL20.glUniform3f(uniforms.get(uniformName),value.x,value.y,value.z);
    }

    public void setUniforms(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniforms(uniformName,pointLights[i],i);
        }
    }

    public void setUniforms(String uniformName, PointLight pointLight, int pos) {
        setUniforms(uniformName+"["+pos+"]", pointLight);
    }

    public void setUniforms(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights!= null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniforms(uniformName,spotLights[i],i);
        }
    }

    public void setUniforms(String uniformName, SpotLight spotLight, int pos) {
        setUniforms(uniformName+"["+pos+"]", spotLight);
    }



    public void setUniforms(String uniformName, Material material){
        setUniforms(uniformName+".ambient",material.getAmbientColour());
        setUniforms(uniformName+".diffuse",material.getDiffuseColour());
        setUniforms(uniformName+".specular",material.getSpecularColour());
        setUniforms(uniformName+".hasTexture",material.hasTexture() ? 1 : 0);
        setUniforms(uniformName+".reflectance",material.getReflectance());
    }

    public void setUniforms(String uniformName, DirectionalLight directionalLight) {
        setUniforms(uniformName+ ".colour",directionalLight.getColour());
        setUniforms(uniformName+ ".direction",directionalLight.getDirection());
        setUniforms(uniformName+ ".intensity",directionalLight.getIntensity());
    }

    public void setUniforms(String uniformName, PointLight pointLight) {
        setUniforms(uniformName + ".colour",pointLight.getColour());
        setUniforms(uniformName + ".position",pointLight.getPosition());
        setUniforms(uniformName + ".intensity",pointLight.getIntensity());
        setUniforms(uniformName + ".constant",pointLight.getConstant());
        setUniforms(uniformName + ".linear",pointLight.getLinear());
        setUniforms(uniformName + ".exponent",pointLight.getExponent());
    }

    public void setUniforms(String name, SpotLight light) {
        setUniforms(name + ".pl.colour",    light.getPointLight().getColour());
        setUniforms(name + ".pl.position",  light.getPointLight().getPosition());
        setUniforms(name + ".pl.intensity", light.getPointLight().getIntensity());
        setUniforms(name + ".pl.constant",  light.getPointLight().getConstant());
        setUniforms(name + ".pl.linear",    light.getPointLight().getLinear());
        setUniforms(name + ".pl.exponent",  light.getPointLight().getExponent());
        setUniforms(name + ".conedir",      light.getConeDirection());
        setUniforms(name + ".cutoff",       light.getCutoff());
    }

    public void setUniforms(String uniformName, boolean value ) {
        float res = 0;
        if (value)
            res = 1;
        GL20.glUniform1f(uniforms.get(uniformName), res);
    }

    public void setUniforms(String uniformName, int value ) {
        GL20.glUniform1i(uniforms.get(uniformName),value);
    }

    public void setUniforms(String uniformName, float value ) {
        GL20.glUniform1f(uniforms.get(uniformName),value);
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode,GL20.GL_VERTEX_SHADER);
    }
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode,GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = GL20.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error Creating Shader. Type : "+ shaderId);
        }

        GL20.glShaderSource(shaderId,shaderCode);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId,GL20.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: Type : " + shaderType + " Info " + GL20.glGetShaderInfoLog(shaderId,1024));
        }

        GL20.glAttachShader(PROGRAMID,shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        GL20.glLinkProgram(PROGRAMID);
        if (GL20.glGetProgrami(PROGRAMID,GL20.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code" + " Info " + GL20.glGetProgramInfoLog(PROGRAMID,1024));
        }

        if (vertexShaderId != 0) {
            GL20.glDetachShader(PROGRAMID,vertexShaderId);
        }

        if (fragmentShaderId != 0) {
            GL20.glDetachShader(PROGRAMID,fragmentShaderId);
        }

        GL20.glValidateProgram(PROGRAMID);
        if (GL20.glGetProgrami(PROGRAMID,GL20.GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Unable to validate shader code: " + GL20.glGetProgramInfoLog(PROGRAMID,1024));
        }
    }

    public void bind() {
        GL20.glUseProgram(PROGRAMID);
    }
    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (PROGRAMID != 0 ) {
            GL20.glDeleteProgram(PROGRAMID);
        }
    }
}
