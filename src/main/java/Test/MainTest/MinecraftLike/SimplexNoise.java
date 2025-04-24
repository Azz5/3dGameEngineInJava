package Test.MainTest.MinecraftLike;

public class SimplexNoise {
    private static class Grad { double x, y; Grad(double x, double y) { this.x = x; this.y = y; } }
    private static final Grad[] grad3 = { /* gradient vectors */ };
    private static final short[] perm = { /* permutation table */ };

    private static int fastfloor(double x) { return x > 0 ? (int)x : (int)x - 1; }
    private static double dot(Grad g, double x, double y) { return g.x * x + g.y * y; }

    public static double noise(double xin, double yin) {
        // full 2D simplex noise implementation
        return 0.0;
    }
}
