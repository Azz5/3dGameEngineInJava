package Test.MainTest.MinecraftLike;

import core.Camera;
import org.joml.Vector3f;

public class BlockSelector {

    // Define the range where we check for blocks.
    private static final float MAX_DISTANCE = 8f;

    // Method to find the nearest block the camera is looking at.
    public static Block getNearestBlock(Camera camera, World world) {
        Vector3f cameraPosition = camera.getPosition();  // Get the camera's position
        Vector3f cameraDirection = camera.getFront();    // Get the camera's forward direction (normalized)

        // Calculate a range of positions to check around the camera's forward direction.
        for (float distance = 0f; distance <= MAX_DISTANCE; distance += 0.1f) {
            // Calculate the position at the current distance in front of the camera
            Vector3f checkPosition = new Vector3f(cameraPosition).add(cameraDirection.mul(distance, new Vector3f()));

            // Get the block at the calculated position (ignoring the Y axis for flat world)
            Block hitBlock = world.getBlockAt((int) Math.floor(checkPosition.x), (int) Math.floor(checkPosition.y), (int) Math.floor(checkPosition.z));

            // If we find a valid block, return it
            if (hitBlock != null && hitBlock.getType() != BlockType.AIR) {
                return hitBlock;  // Return the first block hit
            }
        }
        return null;  // No block was hit in the range
    }
}

