package Test.MainTest.MinecraftLike;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void drawHighlight(Block block) {
        if (block == null) return;

        // Get the block's position (for highlighting)
        Vector3f position = block.getPosition();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glLineWidth(2);
        GL11.glColor3f(1f, 0f, 0f);  // Red outline
        GL11.glTranslatef(position.x, position.y, position.z);

        // Draw wireframe for the selected block
        GL11.glBegin(GL11.GL_QUADS);
        // Front face
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(1, 0, 0);
        GL11.glVertex3f(1, 1, 0);
        GL11.glVertex3f(0, 1, 0);
        // Back face
        GL11.glVertex3f(1, 0, 1);
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(0, 1, 1);
        GL11.glVertex3f(1, 1, 1);
        // Left face
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 1, 0);
        GL11.glVertex3f(0, 1, 1);
        // Right face
        GL11.glVertex3f(1, 0, 0);
        GL11.glVertex3f(1, 0, 1);
        GL11.glVertex3f(1, 1, 1);
        GL11.glVertex3f(1, 1, 0);
        // Top face
        GL11.glVertex3f(0, 1, 0);
        GL11.glVertex3f(1, 1, 0);
        GL11.glVertex3f(1, 1, 1);
        GL11.glVertex3f(0, 1, 1);
        // Bottom face
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(1, 0, 1);
        GL11.glVertex3f(1, 0, 0);
        GL11.glVertex3f(0, 0, 0);
        GL11.glEnd();

        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}



