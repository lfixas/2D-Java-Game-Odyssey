package odyssey.game.windows.Tuto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import odyssey.game.OdysseyGame;

public class Tuto {

    private final Texture pressE = new Texture(Gdx.files.internal("tuto/KeyE.png"));

    private final OdysseyGame game;

    public Tuto(OdysseyGame game) {
        this.game = game;
    }

    public void pressE() {
        game.batch.begin();
        int pressEWidth = pressE.getWidth() * 5, pressEHeight = pressE.getHeight() * 5;
        game.batch.draw(pressE, OdysseyGame.WIDTH / 2 - pressEWidth / 2, OdysseyGame.HEIGHT / 2 + OdysseyGame.HEIGHT / 8, pressEWidth, pressEHeight);
        game.batch.end();
    }
}
