package core;

import org.lwjgl.opengl.GL20;

public class ShaderManager {

    private final int PROGRAMID;

    private int vertexShaderId, fragmentShaderId;


    public ShaderManager() throws Exception {
        PROGRAMID = GL20.glCreateProgram();

        if (PROGRAMID == 0) {
            throw new Exception("Could not create shader");
        }
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
