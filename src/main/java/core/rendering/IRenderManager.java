package core.rendering;

import core.Camera;
import core.entity.Entity;
import core.entity.Model;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.lighting.SpotLight;

public interface IRenderManager<T> {

    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
