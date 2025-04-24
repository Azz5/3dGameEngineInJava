package Test.MainTest;

import core.*;
import core.entity.*;
import core.entity.terrain.BlendMapTerrain;
import core.entity.terrain.Terrain;
import core.entity.terrain.TerrainTexture;
import core.lighting.DirectionalLight;
import core.lighting.PointLight;
import core.lighting.SpotLight;
import core.rendering.RenderManager;
import core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import java.util.Random;


public class TestGame implements ILogic {


    private final RenderManager renderManager;
    private final ObjectLoader loader;
    private final WindowManager windowManager;
    private SceneManager sceneManager;
    private Camera camera;
    Vector3f cameraInc;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = LaunchTest.getWindow();
        loader = new ObjectLoader();
        camera = new Camera(new Vector3f(0,0,0),new Vector3f());
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderManager.init();
        Model model = loader.loadOBJModel("/models/cube_textured_dedup.obj");
        model.setTexture(new Texture(ObjectLoader.loadTexture("textures/grass_block_side.png")),1.0f);

        TerrainTexture backgroundTexture = new TerrainTexture(ObjectLoader.loadTexture("textures/grass_block_side.png"));
        TerrainTexture redTexture = new TerrainTexture(ObjectLoader.loadTexture("textures/Dirt.png"));
        TerrainTexture greenTexture = new TerrainTexture(ObjectLoader.loadTexture("textures/Cobblestone.png"));
        TerrainTexture blueTexture = new TerrainTexture(ObjectLoader.loadTexture("textures/Sand.png"));
        TerrainTexture blendMap = new TerrainTexture(ObjectLoader.loadTexture("textures/blendMap.png"));

        BlendMapTerrain blendMapTerrain = new BlendMapTerrain(backgroundTexture,redTexture,greenTexture,blueTexture);

        Terrain terrain = new Terrain(new Vector3f(0,1,-800),loader,
                new Material(new Texture(ObjectLoader.loadTexture("textures/checkerboard_texture.png")),0.01f),blendMapTerrain,blendMap);
        Terrain terrain1 = new Terrain(new Vector3f(-800,1,-800),loader,
                new Material(new Vector4f(0,0,0,0),0.01f),blendMapTerrain,blendMap);

        sceneManager.addTerrain(terrain); sceneManager.addTerrain(terrain1);

        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            float x = rnd.nextFloat() * 100 - 50;
            float y = rnd.nextFloat() * 100 - 50;
            float z = rnd.nextFloat() * -200;
            sceneManager.addEntity(new Entity(model, new Vector3f(x,y,z),
                    new Vector3f(rnd.nextFloat()*180,rnd.nextFloat() * 180, 0), 1 ));
        }

        sceneManager.addEntity(new Entity(model,new Vector3f(0,0,-2f),new Vector3f(0,0,0),1));

        //entity = new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0),1);



        float lightIntensity = 0.3f;

        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
        Vector3f lightColour = new Vector3f(1,1,1);


        //Pointlight
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity * 0);


        //Spotlight
        Vector3f coneDir = new Vector3f( 0,-50,0);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        lightIntensity = 50000f;
        SpotLight spotLight = new SpotLight(new PointLight(new Vector3f(0,0.25f,0f),new Vector3f(1f,50f,-5f),lightIntensity,0f,0f,0.02f),coneDir,cutoff);
        SpotLight spotLight1 = new SpotLight(new PointLight(new Vector3f(0.25f,0f,0f),new Vector3f(1f,50f,-5f),lightIntensity,0f,0f,0.02f),coneDir,cutoff);


        //DirectionalLight
        lightPosition = new Vector3f(-1,-10,-2);
        lightColour = new Vector3f(1,1,1);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColour,lightPosition,lightIntensity));

        sceneManager.setPointLights(new PointLight[]{pointLight});
        sceneManager.setSpotLights(new SpotLight[]{spotLight,spotLight1});
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

        float lightPos = sceneManager.getSpotLights()[0].getPointLight().getPosition().z;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_N)) {
            sceneManager.getSpotLights()[0].getPointLight().getColour().z = lightPos + 0.1f;
        }
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_M)) {
            sceneManager.getSpotLights()[0].getPointLight().getColour().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED,cameraInc.y * Consts.CAMERA_MOVE_SPEED,cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (mouseInput.isLeftButtonPress()){
            Vector2f rotateVector = mouseInput.getDisplayVector();
            camera.moveRotation(rotateVector.x * Consts.MOUSE_SENSITIVITY,rotateVector.y * Consts.MOUSE_SENSITIVITY,0);
        }

        sceneManager.setSpotAngle(sceneManager.getSpotInc() * 0.15f);
        if (sceneManager.getSpotAngle() > 4) {
            sceneManager.setSpotInc(-1);
        } else if (sceneManager.getSpotAngle() <= 4) {
            sceneManager.setSpotInc(1);
        }

        double spotAngleRad = Math.toRadians(sceneManager.getSpotAngle());
        Vector3f coneDir = sceneManager.getSpotLights()[0].getPointLight().getPosition();
        coneDir.y = (float) Math.sin(spotAngleRad);

        sceneManager.setLightAngle(sceneManager.getLightAngle() + 1f);
        if (sceneManager.getLightAngle()> 90) {
            sceneManager.getDirectionalLight().setIntensity(0);
            if (sceneManager.getLightAngle()>= 360) {
                sceneManager.setLightAngle(-90);
            }
        } else if (sceneManager.getLightAngle()<= -80 || sceneManager.getLightAngle()>= 80) {
            float factor = 1 - (Math.abs(sceneManager.getLightAngle()) - 80) / 10.0f;
            sceneManager.getDirectionalLight().setIntensity(factor);
            sceneManager.getDirectionalLight().getColour().y = Math.max(factor, 0.9f);
            sceneManager.getDirectionalLight().getColour().z = Math.max(factor, 0.5f);
        } else {
            sceneManager.getDirectionalLight().setIntensity(1);
            sceneManager.getDirectionalLight().getColour().x = 1;
            sceneManager.getDirectionalLight().getColour().y = 1;
            sceneManager.getDirectionalLight().getColour().z = 1;
        }
        double andRad = Math.toRadians((sceneManager.getLightAngle()));
        sceneManager.getDirectionalLight().getDirection().x = (float) Math.sin(andRad);
        sceneManager.getDirectionalLight().getDirection().y = (float) Math.cos(andRad);
        for (Entity entity : sceneManager.getEntities()) {
            renderManager.processEntites(entity);
        }

        for (Terrain terrain : sceneManager.getTerrains()) {
            renderManager.processTerrain(terrain);
        }

    }

    @Override
    public void render() {
/*
        if (windowManager.isResize()) {
            GL11.glViewport(0, 0, windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(true);
        }
*/     renderManager.render(camera,sceneManager);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
