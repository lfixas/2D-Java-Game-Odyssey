package odyssey.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.Input.Keys;
import odyssey.game.screens.MainGameScreen;
import odyssey.game.screens.MainMenuScreen;

public class moveAction extends Action
{
    private Player player = null;
    public moveAction(Player player)
    {
        this.player = player;
    }
    public boolean act(float delta)
    {
        boolean moveUp = Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.DOWN);
        boolean moveRight = Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.LEFT);

        if(MainGameScreen.PAUSE) {
            return false;
        }

        if (!moveUp & !moveRight)
            return false;

        if (moveUp)
            this.player.updateMotion(Gdx.input.isKeyPressed(Keys.UP) ? "UP" : "DOWN", delta);

        if (moveRight)
            this.player.updateMotion(Gdx.input.isKeyPressed(Keys.RIGHT) ? "RIGHT" : "LEFT", delta);

        // pour que l'Action soit rethrow en permanence.
        return false;
    }
}
