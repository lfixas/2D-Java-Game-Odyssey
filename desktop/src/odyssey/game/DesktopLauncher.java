package odyssey.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import odyssey.game.OdysseyGame;

import java.io.IOException;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Odyssey");
		config.setWindowedMode(OdysseyGame.WIDTH,OdysseyGame.HEIGHT);
		config.setWindowIcon("icon_64.png");
		config.setResizable(false);

		Settings settings = new Settings();
		settings.maxWidth = 512;
		settings.maxHeight = 512;

		TexturePacker.process(settings, "assets/player", "assets/TexturesPack", "player");

		new Lwjgl3Application(new OdysseyGame(), config);
	}
}
