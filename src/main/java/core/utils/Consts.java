package core.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Objects;

public class Consts {
    public static final String TITLE = "Test";

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    public static final float SPECULAR_POWER = 10f;


    public static final float MOUSE_SENSITIVITY = 0.2f;

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f,1.0f,1.0f,1.0f);

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(1.7f,1.7f,1.7f);

}
