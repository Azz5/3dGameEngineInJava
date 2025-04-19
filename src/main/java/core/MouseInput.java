package core;

import Test.LaunchTest;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {

    private final Vector2d previousPosition, currentPosition;
    private final Vector2f displayVector;

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1,-1);
        currentPosition = new Vector2d(0,0);
        displayVector = new Vector2f();
    }

    public void init() {
        GLFW.glfwSetCursorPosCallback(LaunchTest.getWindow().getWindowHandler(),((window, xpos, ypos) -> {
            currentPosition.x = xpos;
            currentPosition.y = ypos;
        }));

        GLFW.glfwSetCursorEnterCallback(LaunchTest.getWindow().getWindowHandler(),((window, entered) -> {
            inWindow = entered;
        }));

        GLFW.glfwSetMouseButtonCallback(LaunchTest.getWindow().getWindowHandler(),((window, button, action, mods) -> {
            leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        }));
    }

    public void input() {
         displayVector.x = 0;
         displayVector.y = 0;
         if (previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
             double x = currentPosition.x - previousPosition.x;
             double y = currentPosition.y - previousPosition.y;
             boolean rotateX = x != 0;
             boolean rotateY = y != 0;
             if(rotateX)
                 displayVector.y = (float) x;
             if (rotateY)
                 displayVector.x = (float) y;
         }
         previousPosition.x = currentPosition.x;
         previousPosition.y = currentPosition.y;
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }
}
