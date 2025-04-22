package core.rendering;

import Test.MainTest.LaunchTest;
import core.Camera;
import core.ShaderManager;
import core.entity.Entity;
import core.entity.Model;
import core.entity.terrain.Terrain;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.lighting.SpotLight;
import core.utils.Consts;
import core.utils.Transformation;
import core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerrainRender implements IRenderManager {

    ShaderManager shader;
    private List<Terrain> terrains;

    public TerrainRender() throws Exception{
        terrains = new ArrayList<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource("/shader/terrain_vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shader/terrain_fragment.fs"));
        shader.link();
        shader.createUniform("backgroundTexture");
        shader.createUniform("redTexture");
        shader.createUniform("greenTexture");
        shader.createUniform("blueTexture");
        shader.createUniform("blendMap");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower" );
        shader.createDirectionalLightUnifrom("directionalLight");
        shader.createPointLightListUniform("pointLights", Consts.MAX_POINT_LIGHTS);
        shader.createSpotLightListUniform("spotLights", Consts.MAX_SPOT_LIGHTS);
    }

    @Override
    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {

        shader.bind();
        shader.setUniforms("projectionMatrix", LaunchTest.getWindow().updateProjectMatrix());
        RenderManager.renderLights(shader,pointLights,spotLights,directionalLight);
        for (Terrain terrain: terrains) {
            bind(terrain.getModel());
            prepare(terrain, camera);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbind();
        }
        terrains.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        shader.setUniforms("backgroundTexture", 0 );
        shader.setUniforms("redTexture", 1 );
        shader.setUniforms("greenTexture", 2 );
        shader.setUniforms("blueTexture", 3 );
        shader.setUniforms("blendMap", 4 );

        shader.setUniforms("material",model.getMaterial());

    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object terrain, Camera camera) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((Terrain) terrain).getBlendMapTerrain().getBackground().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((Terrain) terrain).getBlendMapTerrain().getRedTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((Terrain) terrain).getBlendMapTerrain().getGreenTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((Terrain) terrain).getBlendMapTerrain().getBlueTexture().getId());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((Terrain) terrain).getBlendMap().getId());

        shader.setUniforms("transformationMatrix", Transformation.createTransformationMatrix((Terrain) terrain));
        shader.setUniforms("viewMatrix", Transformation.getViewMatrix(camera));

    }


    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public List<Terrain> getTerrain() {
        return terrains;
    }
}
