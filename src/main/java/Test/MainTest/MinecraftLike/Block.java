package Test.MainTest.MinecraftLike;

import org.joml.Vector3f;

/**
 * Simple block data.
 */
public class Block {
    public BlockType type;
    public Vector3f position;

    public Block(BlockType type, Vector3f position) {
        this.type = type;
        this.position = position;
    }

    public BlockType getType() {
        return type;
    }

    public Vector3f getPosition() {
        return position;
    }
}
