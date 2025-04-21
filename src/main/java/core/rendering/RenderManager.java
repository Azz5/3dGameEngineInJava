package core.rendering;

import Test.MainTest.LaunchTest;
import core.Camera;
import core.ShaderManager;
import core.WindowManager;
import core.entity.Entity;
import core.entity.Model;
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

public class RenderManager {

    private final WindowManager window;
    private EntityRender entityRender;


    public RenderManager() {
        window = LaunchTest.getWindow();
    }

    public void init() throws Exception {
        entityRender = new EntityRender();

        entityRender.init();
    }

    public static void renderLights(ShaderManager shader, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
        shader.setUniforms("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniforms("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;

        for (int i = 0; i < numLights; i++) {
            shader.setUniforms("spotLights",spotLights[i],i);
        }

        numLights = spotLights != null ? pointLights.length : 0;

        for (int i = 0; i < numLights; i++) {
            shader.setUniforms("pointLights",pointLights[i],i);
        }

        shader.setUniforms("directionalLight", directionalLight);
    }
    public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
        clear();

        if (window.isResize()) {
            GL20.glViewport(0,0,window.getWidth(),window.getHeight());
            window.setResize(false);
        }

        entityRender.render(camera,pointLights,spotLights,directionalLight);

    }

    public void processEntites(Entity entity) {
        List<Entity> entityList = entityRender.getEntities().get(entity.getModel());
        if (entityList != null) {
            entityList.add(entity);
        } else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRender.getEntities().put(entity.getModel(),newEntityList);
        }
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRender.cleanup();
    }
}
