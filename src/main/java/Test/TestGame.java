package Test;

import core.*;
import core.entity.Entity;
import core.entity.Model;
import core.entity.Texture;
import core.lighting.DirectionalLight;
import core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;


public class TestGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.05f;

    private final RenderManager renderManager;
    private final ObjectLoader loader;
    private final WindowManager windowManager;

    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = LaunchTest.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderManager.init();

        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("C:\\Users\\FONDE\\FastProgramming\\VoxelEngineFolder\\VoxelGameEngine\\src\\main\\resources\\textures\\grass_block_side.png")),1f);
        entity = new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0),1);

        float lightIntensity = 0.0f;
        Vector3f lightPosition = new Vector3f(-1,-10,0);
        Vector3f lightColour = new Vector3f(1,1,1);
        directionalLight= new DirectionalLight(lightColour,lightPosition,lightIntensity);
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED,cameraInc.y * CAMERA_MOVE_SPEED,cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouseInput.isLeftButtonPress()){
            Vector2f rotateVector = mouseInput.getDisplayVector();
            camera.moveRotation(rotateVector.x * Consts.MOUSE_SENSITIVITY,rotateVector.y * Consts.MOUSE_SENSITIVITY,0);
        }
        //entity.incRotation(0.0f,0.25f,0.0f);
        lightAngle += 0.5f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360){
                lightAngle = -90;
            }
        }else if (lightAngle<= -80 || lightAngle>= 80) {
            float factor = 1-(Math.abs(lightAngle)-80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor,0.9f);
            directionalLight.getColour().z = Math.max(factor,0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x = 1;
            directionalLight.getColour().y = 1;
            directionalLight.getColour().z = 1;
        }
        double andRad = Math.toRadians((lightAngle));
        directionalLight.getDirection().x = (float) Math.sin(andRad);
        directionalLight.getDirection().y = (float) Math.cos(andRad);
    }

    @Override
    public void render() {
/*
        if (windowManager.isResize()) {
            GL11.glViewport(0, 0, windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(true);
        }
*/
        renderManager.render(entity,camera,directionalLight);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
