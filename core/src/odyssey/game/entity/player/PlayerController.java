package odyssey.game.entity.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import odyssey.game.OdysseyGame;
import odyssey.game.screens.MainGameScreen;

public class PlayerController implements InputProcessor
{
    public PlayerController(Player player)
    {
        this.player = player;
    }
    public static Player getPlayer() { return player; }
    public boolean keyDown(int keyCode)
    {

        if (MainGameScreen.PAUSE)
            return false;

        if (keyCode == Input.Keys.I)
        {
            this.player.displayInventory();

            return true;
        }

        if (keyCode == Input.Keys.E)
        {
            Vector2 key = new Vector2(OdysseyGame.WIDTH / 2 , OdysseyGame.HEIGHT / 2);

            this.player.clicHandle(key);

            return false;
        }

        return false;
    }
    public boolean keyUp(int keyCode)
    {
        return false;
    }
    public boolean keyTyped(char c)
    {
        return false;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (MainGameScreen.PAUSE)
            return false;

        if (button == Input.Buttons.LEFT)
        {
            Vector2 clic = new Vector2(screenX, screenY);

            this.player.clicHandle(clic);

            return false;
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    public boolean scrolled(float amount, float param)
    {
        return false;
    }

    private Viewport viewport = null;
    private static Player player = null;
}
