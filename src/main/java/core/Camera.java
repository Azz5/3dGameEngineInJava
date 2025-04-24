package core;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Vector3f position, rotation;
    private float pitch, yaw;
    private static final float SPEED = 10.0f;
    private static final float SENSITIVITY = 0.1f;

    public Camera(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        pitch = 0; yaw = -90;
    }
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void processInput(float interval, core.WindowManager window, core.MouseInput mouse) {
        // Keyboard movement
        float velocity = SPEED * interval;
        if (window.isKeyPressed(GLFW_KEY_W)) movePosition(0, 0, -velocity);
        if (window.isKeyPressed(GLFW_KEY_S)) movePosition(0, 0, velocity);
        if (window.isKeyPressed(GLFW_KEY_A)) movePosition(-velocity, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_D)) movePosition(velocity, 0, 0);

        // Mouse look
        mouse.input();
        org.joml.Vector2f mv = mouse.getDisplayVector();
        yaw   += mv.x * SENSITIVITY;
        pitch -= mv.y * SENSITIVITY;
        pitch  = Math.max(-89f, Math.min(89f, pitch));
    }
/*
    private void movePosition(float dx, float dy, float dz) {
        // forward/back
        position.x += (float)Math.sin(Math.toRadians(yaw)) * -dz;
        position.z += (float)Math.cos(Math.toRadians(yaw)) * dz;
        // strafe
        position.x += (float)Math.sin(Math.toRadians(yaw - 90)) * -dx;
        position.z += (float)Math.cos(Math.toRadians(yaw - 90)) * dx;
        // up/down
        position.y += dy;
    }
    */

    public Camera() {
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    public void movePosition(float x, float y, float z) {
        if (z!=0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if (x!=0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }

        position.y += y;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x,float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    public void moveRotation(float x,float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setPosition(Vector3f vector3f) {
        setPosition(vector3f.x,vector3f.y,vector3f.z);
    }


    public Vector3f getFront() {
        Vector3f dir = new Vector3f(
                (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch)),
                (float) Math.sin(Math.toRadians(pitch)),
                (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch))
        );
        return dir.normalize();
    }
}
