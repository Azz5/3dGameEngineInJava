package Test;

import core.EngineManager;
import core.WindowManager;
import core.utils.Consts;

public class LaunchTest {
    private static WindowManager window;
    private static TestGame game;
    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE,890,540,false);
        game = new TestGame();
        EngineManager engineManager = new EngineManager();

        try {
            engineManager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
