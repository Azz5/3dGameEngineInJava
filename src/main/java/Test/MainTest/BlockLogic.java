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
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class BlockLogic implements ILogic {
    private WindowManager   window;
    private RenderManager   renderer;
    private ObjectLoader    loader;
    private Camera          camera;
    private DirectionalLight sun;
    private PointLight      pointLight;
    private SpotLight       spotLight;
    private Model           cubeModel;
    private Texture         cubeTexture;
    private List<Entity>    blocks = new ArrayList<>();

    @Override
    public void init() throws Exception {
        System.out.println(">>> GameLogic.init()");

        window     = LaunchTest.getWindow();
        renderer   = new RenderManager();
        loader     = new ObjectLoader();
        camera     = new Camera(new Vector3f(10, 10, 10), new Vector3f(-30, -45, 0));
        int size = 10;

        float half = (size - 1) / 2.0f;

// put the camera above the center, pulled back
        camera = new Camera(
                new Vector3f(half, size* 1.2f, size * 1.5f),
                // look down at roughly –30° pitch, face towards negative Z
                new Vector3f(-30f, 180f, 0f)
        );
        // === lights ===
        sun        = new DirectionalLight(new Vector3f(1f,1f,1f),
                new Vector3f(-1f,-1f,-1f),
                0.8f);
        pointLight = new PointLight(new Vector3f(1f,1f,1f),
                camera.getPosition(),
                1.0f);
        spotLight  = new SpotLight(pointLight,
                new Vector3f(0f,-1f,0f),
                (float)Math.cos(Math.toRadians(15)));

        // === renderer setup ===
        renderer.init();

        System.out.println(">>> init(): blocks.size() = " + blocks.size());
        if (!blocks.isEmpty()) {
            System.out.println("    first block at " + blocks.get(0).getPos() +
                    ", last block at " + blocks.get(blocks.size() - 1).getPos());
        }

        // === build a cube mesh ===
        float[] vertices = {
                // front face
                -0.5f, 0.5f, 0.5f,   -0.5f,-0.5f, 0.5f,    0.5f,-0.5f, 0.5f,    0.5f, 0.5f, 0.5f,
                // back face
                0.5f, 0.5f,-0.5f,    0.5f,-0.5f,-0.5f,   -0.5f,-0.5f,-0.5f,   -0.5f, 0.5f,-0.5f,
                // left face
                -0.5f, 0.5f,-0.5f,   -0.5f,-0.5f,-0.5f,   -0.5f,-0.5f, 0.5f,   -0.5f, 0.5f, 0.5f,
                // right face
                0.5f, 0.5f, 0.5f,    0.5f,-0.5f, 0.5f,    0.5f,-0.5f,-0.5f,    0.5f, 0.5f,-0.5f,
                // top face
                -0.5f, 0.5f,-0.5f,   -0.5f, 0.5f, 0.5f,    0.5f, 0.5f, 0.5f,    0.5f, 0.5f,-0.5f,
                // bottom face
                -0.5f,-0.5f, 0.5f,   -0.5f,-0.5f,-0.5f,    0.5f,-0.5f,-0.5f,    0.5f,-0.5f, 0.5f
        };
        float[] texCoords = {
                // each face reuses the same UV quad
                0,0,  0,1,  1,1,  1,0,
                0,0,  0,1,  1,1,  1,0,
                0,0,  0,1,  1,1,  1,0,
                0,0,  0,1,  1,1,  1,0,
                0,0,  0,1,  1,1,  1,0,
                0,0,  0,1,  1,1,  1,0
        };
        float[] normals = {
                // front
                0,0,1,  0,0,1,  0,0,1,  0,0,1,
                // back
                0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,
                // left
                -1,0,0, -1,0,0, -1,0,0, -1,0,0,
                // right
                1,0,0,  1,0,0,  1,0,0,  1,0,0,
                // top
                0,1,0,  0,1,0,  0,1,0,  0,1,0,
                // bottom
                0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0
        };
        int[] indices = {
                0,1,2,  2,3,0,      // front
                4,5,6,  6,7,4,      // back
                8,9,10, 10,11,8,    // left
                12,13,14, 14,15,12, // right
                16,17,18, 18,19,16, // top
                20,21,22, 22,23,20  // bottom
        };

        cubeModel   = loader.loadModel(vertices, texCoords, normals, indices);
        cubeTexture = new Texture(loader.loadTexture("C:\\Users\\FONDE\\FastProgramming\\VoxelEngineFolder\\VoxelGameEngine\\src\\main\\resources\\textures\\grassblock.png"));
        cubeModel.setTexture(cubeTexture);

        // === scatter a 20×20 block “floor” at y=0 ===
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                blocks.add(new Entity(
                        cubeModel,
                        new Vector3f(x, 0, z),
                        new Vector3f(0,0,0),
                        1f
                ));
            }
        }
    }

    @Override
    public void input() {
        // WASD + space/shift flight
        if (window.isKeyPressed(GLFW.GLFW_KEY_W))        camera.movePosition(0, 0, -0.2f);
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))        camera.movePosition(0, 0,  0.2f);
        if (window.isKeyPressed(GLFW.GLFW_KEY_A))        camera.movePosition(-0.2f, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))        camera.movePosition( 0.2f, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE))    camera.movePosition(0,  0.2f, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) camera.movePosition(0, -0.2f, 0);
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        // mouse‐look
        Vector2f dv = mouse.getDisplayVector();
        camera.moveRotation(
                dv.y * Consts.MOUSE_SENSITIVITY,
                dv.x * Consts.MOUSE_SENSITIVITY,
                0
        );
        // flashlight follows camera
        pointLight.setPosition(camera.getPosition());
        spotLight.getPointLight().setPosition(camera.getPosition());
        // (you can compute cone direction from camera rotation if you like)
    }
    @Override
    public void render() {
        // 1) clear exactly once
        renderer.clear();

        // 2) log how many blocks you actually have
        System.out.println(">>> render(): blocks.size() = " + blocks.size());

        // 3) draw each block and log its position
        for (int i = 0; i < blocks.size(); i++) {
            Entity block = blocks.get(i);
            System.out.println("    drawing block[" + i + "] at " + block.getPos());
            //renderer.render(block, camera, sun, pointLight, spotLight);
            int err = GL11.glGetError();
            if (err != GL11.GL_NO_ERROR) {
                System.err.println("      GL error: 0x" + Integer.toHexString(err));
            }
        }
    }



    @Override
    public void cleanup() {
        loader.cleanup();
        renderer.cleanup();
    }
}

