package Test.MainTest.MinecraftLike;

import Test.MainTest.LaunchTest;
import core.*;
import core.entity.*;
import core.entity.terrain.Terrain;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.rendering.RenderManager;
import core.utils.Consts;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BlockGame implements ILogic {
    private RenderManager renderer;
    private Camera camera;
    private SceneManager sceneManager;
    private MouseInput mouseInput;
    private WindowManager windowManager;
    private Model blockModel;
    private List<Entity> blocks = new ArrayList<>();
    private Vector3f cameraInc;
    ObjectLoader loader = new ObjectLoader();
    public BlockGame() {
        renderer= new RenderManager();
        windowManager = LaunchTest.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer = new RenderManager();
        renderer.init();

        camera = new Camera();
        sceneManager = new SceneManager(0);
        sceneManager.setAmbientLight(0.3f, 0.3f, 0.3f);
        sceneManager.setDirectionalLight(new DirectionalLight(
                new Vector3f(1, 1, 1), new Vector3f(-0.5f, -1, -0.5f), 1.0f));

        // Load block model (simple cube)
        blockModel = loader.loadOBJModel("/models/cube.obj");

        // Add ground terrain
        Terrain terrain = new Terrain(
                new Vector3f(0, -5, 0),
                ObjectLoader.getInstance(),
                new Material(new Texture(ObjectLoader.loadTexture("textures/Sand.png"))),
                null, null
        );
        sceneManager.addTerrain(terrain);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (windowManager.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_1)) addBlock();
        if (windowManager.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_2)) removeBlock();
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_W)) cameraInc.z = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_S)) cameraInc.z = 1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_A)) cameraInc.x = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_D)) cameraInc.x = 1;
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(
                cameraInc.x * 0.3f,
                cameraInc.y * 0.3f,
                cameraInc.z * 0.3f
        );
        camera.moveRotation(
                mouseInput.getDisplayVector().x * 0.1f,
                mouseInput.getDisplayVector().y * 0.1f,
                0
        );
    }

    @Override
    public void render() {
        renderer.render(camera, sceneManager);
    }

    private void addBlock() {
        Vector3f position = new Vector3f(camera.getPosition())
                .add(camera.getForwardVector().mul(5));
        Vector3f rotation = new Vector3f(camera.getRotation())
                .add(camera.getForwardVector().mul(5));

        Entity block = new Entity(blockModel, position,rotation,1);
        sceneManager.addEntity(block);
        blocks.add(block);
    }

    private void removeBlock() {
        if (!blocks.isEmpty()) {
            Entity lastBlock = blocks.remove(blocks.size() - 1);
            sceneManager.getEntities().remove(lastBlock);
        }
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}