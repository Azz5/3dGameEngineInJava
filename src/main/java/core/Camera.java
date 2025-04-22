package core;

import org.joml.Vector3f;

public class Camera {

    private Vector3f position, rotation;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

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

    public Vector3f getForwardVector() {
        // Convert rotation angles from degrees to radians
        float yaw = (float) Math.toRadians(rotation.y);
        float pitch = (float) Math.toRadians(rotation.x);

        // Calculate forward vector components
        float x = (float) (Math.cos(pitch) * Math.sin(yaw));
        float y = (float) -Math.sin(pitch);
        float z = (float) (Math.cos(pitch) * Math.cos(yaw));

        Vector3f forward = new Vector3f(x, y, z);
        return forward.normalize(); // Return normalized vector
    }
}
