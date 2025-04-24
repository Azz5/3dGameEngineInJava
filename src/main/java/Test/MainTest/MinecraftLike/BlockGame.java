package Test.MainTest.MinecraftLike;

import core.*;
import core.entity.Entity;
import core.entity.SceneManager;
import core.entity.Texture;
import core.rendering.RenderManager;
import core.utils.Consts;
import core.entity.Model;
import org.joml.Vector3f;
import org.joml.Vector2f;
import Test.MainTest.LaunchTest;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Simplest break/build demo on 16×16 flat world.
 */
public class BlockGame implements ILogic {
    private RenderManager renderManager;
    private core.Camera camera;
    private SceneManager sceneManager;
    private ObjectLoader loader;
    private Model cubeModel;
    private WindowManager windowManager;
    private World world;

    @Override
    public void init() throws Exception {
        // load cube procedurally (unit cube)
        loader = ObjectLoader.getInstance();
        windowManager = LaunchTest.getWindow();
        // Lock the mouse cursor
        //GLFW.glfwSetInputMode(windowManager.getWindowHandler(), GLFW.GLFW_CURSOR, org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED);

        float[] positions = {
                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f
        };
        float[] textureCoords = {
                0,0, 1,0, 1,1, 0,1,
                0,0, 1,0, 1,1, 0,1
        };
        float[] normals = {
                0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1,
                0, 0,-1,  0, 0,-1,  0, 0,-1,  0, 0,-1
        };
        int[] indices = {
                0,1,2, 2,3,0, // front
                4,5,6, 6,7,4, // back
                3,2,6, 6,5,3, // top
                0,4,7, 7,1,0, // bottom
                0,3,5, 5,4,0, // left
                1,7,6, 6,2,1  // right
        };
        cubeModel = loader.loadModel(positions, textureCoords, normals, indices);
        int tex = ObjectLoader.loadTexture("textures/Dirt.png");
        cubeModel.setTexture(new Texture(tex));

        // initialize renderer


        // camera and lighting
 //       camera = new core.Camera(new Vector3f(8, 1, 8), new Vector3f(45, -45, 0));
// Camera should start at (0, 10, 0) and look towards (0, 0, -1) in the z-axis
        camera = new Camera(new Vector3f(8, 1 ,8), new Vector3f(0, 0, 0));  // Position at (0, 10, 0) looking along z-axis

        sceneManager = new SceneManager(0f);
        sceneManager.setAmbientLight(1f, 1f, 1f);
        sceneManager.setDirectionalLight(new core.lighting.DirectionalLight(
                new org.joml.Vector3f(1f,1f,1f), new org.joml.Vector3f(0f,-1f,0f), 1f
        ));
        sceneManager.setPointLights(new core.lighting.PointLight[]{});
        sceneManager.setSpotLights(new core.lighting.SpotLight[]{});
        // simple world
        world = new World(loader, cubeModel);
        renderManager = new RenderManager();
        renderManager.init();
    }

    @Override
    public void input() {
        // no-op

    }
    @Override
    public void update(float interval, MouseInput mouseInput) {
        WindowManager window = LaunchTest.getWindow();
        //mouseInput.input();
        Vector2f mv = mouseInput.getDisplayVector();
        camera.moveRotation(mv.x * Consts.MOUSE_SENSITIVITY, mv.y * Consts.MOUSE_SENSITIVITY, 0);

        // Camera movement logic (WASD keys)
        float v = Consts.CAMERA_MOVE_SPEED * interval;
        if (window.isKeyPressed(GLFW_KEY_W)) camera.movePosition(0, 0, -v);
        if (window.isKeyPressed(GLFW_KEY_S)) camera.movePosition(0, 0, v);
        if (window.isKeyPressed(GLFW_KEY_A)) camera.movePosition(-v, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_D)) camera.movePosition(v, 0, 0);

        // Get the nearest block the camera is looking at (block interaction)
        Block hitBlock = BlockSelector.getNearestBlock(camera, world);
        System.out.println(hitBlock);
        if (hitBlock != null) {
            // Highlight the block (optional visualization, you can skip this step if not needed)
            RenderUtils.drawHighlight(hitBlock);

            // Place or break the block
            if (mouseInput.isLeftButtonPress()) {
                world.removeBlock((int) hitBlock.getPosition().x, (int) hitBlock.getPosition().y, (int) hitBlock.getPosition().z);
            } else if (mouseInput.isRightButtonPress()) {
                world.addBlock((int) hitBlock.getPosition().x, (int) hitBlock.getPosition().y, (int) hitBlock.getPosition().z);
            }
        }

        // Process other entities in the world
        for (Entity e : world.getAllEntities()) {
            renderManager.processEntites(e);
        }
    }


    @Override
    public void render() {
        renderManager.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
