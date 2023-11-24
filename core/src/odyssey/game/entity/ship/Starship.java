package odyssey.game.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class Starship
{
    private static int state = 5;

    public static void repair()
    {
        state = MathUtils.clamp( state + 5, 0, 100);
    }
    public static int getState() { return state; }
    public static void setState(int value) { state = MathUtils.clamp(value, 0, 100); }
}
