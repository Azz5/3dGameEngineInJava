package Test.MainTest;

import core.*;
import core.entity.Entity;
import core.entity.Model;
import core.entity.Texture;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.lighting.SpotLight;
import core.rendering.RenderManager;
import core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TestGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.05f;

    private final RenderManager renderManager;
    private final ObjectLoader loader;
    private final WindowManager windowManager;

    private List<Entity> entities;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;
    private DirectionalLight directionalLighter;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;

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
        Model model = loader.loadOBJModel("/models/cube_textured_dedup.obj");
        model.setTexture(new Texture(loader.loadTexture("C:\\Users\\FONDE\\FastProgramming\\VoxelEngineFolder\\VoxelGameEngine\\src\\main\\resources\\textures\\grass_block_side.png")),1.0f);

        entities = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            float x = rnd.nextFloat() * 100 - 50;
            float y = rnd.nextFloat() * 100 - 50;
            float z = rnd.nextFloat() * -200;
            entities.add(new Entity(model, new Vector3f(x,y,z),
                    new Vector3f(rnd.nextFloat()*180,rnd.nextFloat() * 180, 0), 1 ));
        }

        entities.add(new Entity(model,new Vector3f(0,0,-2f),new Vector3f(0,0,0),1));

        //entity = new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0),1);



        float lightIntensity = 0.3f;

        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
        Vector3f lightColour = new Vector3f(1,1,1);


        //Pointlight
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity * 0);


        //Spotlight
        Vector3f coneDir = new Vector3f(-1,-10,3);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColour,new Vector3f(0,0,1f),lightIntensity),coneDir,cutoff);
        SpotLight spotLight1 = new SpotLight(pointLight,coneDir,cutoff);


        //DirectionalLight
        lightPosition = new Vector3f(-1,-10,-2);
        lightColour = new Vector3f(1,1,1);
        directionalLight= new DirectionalLight(lightColour,lightPosition,lightIntensity);

        pointLights = new PointLight[]{pointLight};
        spotLights = new SpotLight[]{spotLight,spotLight1};
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -5;
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

        float lightPos = spotLights[0].getPointLight().getPosition().z;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_N)) {
            spotLights[0].getPointLight().getColour().z = lightPos + 0.1f;
        }
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_M)) {
            spotLights[0].getPointLight().getColour().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED,cameraInc.y * CAMERA_MOVE_SPEED,cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouseInput.isLeftButtonPress()){
            Vector2f rotateVector = mouseInput.getDisplayVector();
            camera.moveRotation(rotateVector.x * Consts.MOUSE_SENSITIVITY,rotateVector.y * Consts.MOUSE_SENSITIVITY,0);
        }
        //entity.incRotation(0.0f,0.25f,0.0f);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_O)) {
            lightAngle += 0.5f;
            if (lightAngle > 90) {
                directionalLight.setIntensity(0);
                if (lightAngle >= 360) {
                    lightAngle = -90;
                }
            } else if (lightAngle <= -80 || lightAngle >= 80) {
                float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
                directionalLight.setIntensity(factor);
                directionalLight.getColour().y = Math.max(factor, 0.9f);
                directionalLight.getColour().z = Math.max(factor, 0.5f);
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
        for (Entity entity : entities) {
            renderManager.processEntites(entity);
        }

    }

    @Override
    public void render() {
/*
        if (windowManager.isResize()) {
            GL11.glViewport(0, 0, windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(true);
        }
*/     renderManager.render(camera,directionalLight,pointLights,spotLights);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
