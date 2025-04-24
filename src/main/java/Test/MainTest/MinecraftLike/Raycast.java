package Test.MainTest.MinecraftLike;
import core.Camera;
import org.joml.Vector3f;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Raycast {
    public static BlockHit getTargetBlock(Camera camera, World world, float maxDistance) {
        Vector3f origin = camera.getPosition();  // Get the camera's position
        Vector3f direction = camera.getFront();  // The camera's forward direction (normalized)

        // Debug: Check if the direction is correct
       // System.out.println("Raycast origin: " + origin + ", direction: " + direction);

        for (float distance = 0f; distance <= maxDistance; distance += 0.1f) {
            Vector3f pos = new Vector3f(origin).add(direction.mul(distance, new Vector3f()));

            // Debug: Print each position the ray is checking

            Block hitBlock = world.getBlockAt((int) Math.floor(pos.x), (int) Math.floor(pos.y), (int) Math.floor(pos.z));

            if (hitBlock != null && hitBlock.getType() != BlockType.AIR) {
                // Debug: When a block is hit
                System.out.println("Block hit at: " + pos);
                return new BlockHit(pos, direction);
            }
        }

        // Debug: No block hit
        System.out.println("No block hit within maxDistance.");
        return null;
    }

    public static class BlockHit {
        public final Vector3f position;
        public final Vector3f normal;

        public BlockHit(Vector3f position, Vector3f normal) {
            this.position = position;
            this.normal = normal;
        }
    }
}

