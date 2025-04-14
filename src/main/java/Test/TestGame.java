package Test;

import core.ILogic;
import core.ObjectLoader;
import core.RenderManager;
import core.WindowManager;
import core.entity.Model;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;


public class TestGame implements ILogic {

    private int direction = 0;
    private float colour = 0.0f;

    private final RenderManager renderManager;
    private final ObjectLoader loader;
    private final WindowManager windowManager;

    private Model model;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = LaunchTest.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderManager.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        model = loader.loadModel(vertices);
    }

    @Override
    public void input() {
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_UP)){
            direction = 1;
        } else if (windowManager.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update() {
        colour += direction * 0.1f;

        if (colour > 1) {
            colour = 1.0f;
        } else if (colour < 0) {
            colour = 0.0f;
        }

    }

    @Override
    public void render() {

        if (windowManager.isResize()) {
            GL11.glViewport(0,0,windowManager.getWidth(),windowManager.getHeight());
            windowManager.setResize(true);
        }

        windowManager.setClearColor(colour,colour,colour,0.0f);
        renderManager.render(model);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
