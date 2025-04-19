package core;

import Test.LaunchTest;
import core.entity.Entity;
import core.entity.Model;
import core.lighting.DirectionalLight;
import core.utils.Consts;
import core.utils.Transformation;
import core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {

    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        window = LaunchTest.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shader/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shader/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower" );
        shader.createDirectionalLightUnifrom("directionalLight");
    }

    public void render(Entity entity, Camera camera, DirectionalLight directionalLight) {
        clear();

        shader.bind();
        shader.setUniforms("textureSampler", 0 );
        shader.setUniforms("transformationMatrix", Transformation.createTransformationMatrix(entity));
        shader.setUniforms("projectionMatrix", window.updateProjectMatrix());
        shader.setUniforms("viewMatrix", Transformation.getViewMatrix(camera));
        shader.setUniforms("material",entity.getModel().getMaterial());
        shader.setUniforms("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniforms("specularPower", Consts.SPECULAR_POWER);
        shader.setUniforms("directionalLight", directionalLight);

        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,entity.getModel().getTexture().getId());
        GL11.glDrawElements(GL11.GL_TRIANGLES,entity.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }
}
