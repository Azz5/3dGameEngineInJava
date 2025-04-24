package Test.MainTest.MinecraftLike;

import core.ObjectLoader;
import core.entity.Entity;
import core.entity.Model;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 * Single 16×16 flat world.
 */
public class World {
    public static final int SIZE = 16;
    private final Block[][] grid = new Block[SIZE][SIZE];
    private final ObjectLoader loader;
    private final Model cubeModel;

    public World(ObjectLoader loader, Model cubeModel) {
        this.loader = loader;
        this.cubeModel = cubeModel;
        // Initialize flat layer of dirt
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                grid[x][z] = new Block(BlockType.DIRT, new Vector3f(x, 0, z));
                System.out.println(x +" " + z + " Block Number " + (x+z));
            }
        }
    }

    /**
     * Get all block entities for rendering.
     */
    public List<Entity> getAllEntities() {
        List<Entity> list = new ArrayList<>();
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                Block b = grid[x][z];
                if (b.type != BlockType.AIR) {
                    Entity e = new Entity(cubeModel,
                            b.position, new Vector3f(0, 0, 0), 1);
                    list.add(e);
                }
            }
        }
        return list;
    }

    /**
     * Convert world position to block grid coords, or return null.
     */
    public Block getBlockAt(int bx, int by, int bz) {
        if (by != 0) return null;
        if (bx < 0 || bx >= SIZE || bz < 0 || bz >= SIZE) return null;
        return grid[bx][bz];
    }

    /**
     * Break block at coords.
     */
    public void removeBlock(int bx, int by, int bz) {
        Block b = getBlockAt(bx, by, bz);
        if (b != null) {
            grid[bx][bz].type = BlockType.AIR;
        }
    }

    /**
     * Place dirt at coords if empty.
     */
    public void addBlock(int bx, int by, int bz) {
        Block b = getBlockAt(bx, by, bz);
        if (b != null && b.type == BlockType.AIR) {
            grid[bx][bz].type = BlockType.DIRT;
        }
    }
}