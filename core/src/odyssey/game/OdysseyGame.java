package odyssey.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import odyssey.game.screens.MainMenuScreen;

public class OdysseyGame extends Game {

	public static int HEIGHT = 720 ;

	public static int WIDTH = 16 * HEIGHT / 9;

	public static int SIZE = 3;

	public static float BGM_VOLUME = 1.0f;

	public static float SE_VOLUME = 1.0f;

	public static boolean SAFE_MODE = true;

	public SpriteBatch batch;
	public static Settings settings;

	@Override
	public void create () {
		batch = new SpriteBatch();

		// SETTINGS
		settings = new Settings();
		settings.loadSettings();
		HEIGHT = Settings.HEIGHT;
        WIDTH = 16 * HEIGHT / 9;
		SIZE = Settings.SIZE;
		BGM_VOLUME = Settings.BGM_VOLUME;
		SE_VOLUME = Settings.SE_VOLUME;
		SAFE_MODE = Settings.SAFE_MODE;
		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);

		// START GAME
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {

	}
}