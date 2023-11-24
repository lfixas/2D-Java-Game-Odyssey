package odyssey.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import odyssey.game.KeyMenu.KeyHandler;
import odyssey.game.OdysseyGame;
import odyssey.game.screens.MainMenuScreen;

import java.security.Key;

public class MainSettingsWindows {

    private static MainMenuScreen menu;

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    private ShapeRenderer shape = new ShapeRenderer();

    private KeyHandler HandleSettingStatus = new KeyHandler("y", 0, 5, 1);

    private static Sound option_success = Gdx.audio.newSound(Gdx.files.internal("sounds/option_success.ogg"));

    private static boolean IsOpen = false;

    KeyHandler HandleSlider_BGM_VOLUME = null;
    private float BGM_VOLUME = OdysseyGame.BGM_VOLUME;
    KeyHandler HandleSlider_SE_VOLUME = null;
    private float SE_VOLUME = OdysseyGame.SE_VOLUME;

    private boolean SAFE_MODE = OdysseyGame.SAFE_MODE;

    public void OpenSettings() {
        IsOpen = true;
    }

    public void DrawSettings(OdysseyGame game, MainMenuScreen menu) {
        this.menu = menu;

        float Box_Pause_X = OdysseyGame.WIDTH * 0.2f;
        float Box_Pause_Y = OdysseyGame.HEIGHT * 0.1f;

        float Box_Pause_WIDTH = OdysseyGame.WIDTH * 0.6f;
        float Box_Pause_HEIGHT = OdysseyGame.HEIGHT * 0.8f;

        float Text_Pause_HEIGHT = (Box_Pause_HEIGHT - Box_Pause_Y) / 10;
        float Text_Pause_WIDTH = (Box_Pause_WIDTH - Box_Pause_X);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);
        shape.rect(Box_Pause_X, Box_Pause_Y, Box_Pause_WIDTH, Box_Pause_HEIGHT);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect(Box_Pause_X + 10, Box_Pause_Y + 10, Box_Pause_WIDTH - 20, Box_Pause_HEIGHT - 20);
        shape.end();

        HandleSettingStatus.KeyHandlerInput();

        game.batch.begin();
        font.setColor(Color.WHITE);

        font.draw(game.batch, "SETTINGS", Box_Pause_X + Text_Pause_WIDTH / 2, Box_Pause_Y + Text_Pause_HEIGHT * 10.5f);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 0) {
            font.setColor(Color.YELLOW);
            if(HandleSlider_BGM_VOLUME == null) {
                HandleSlider_BGM_VOLUME = new KeyHandler("x", BGM_VOLUME, 1.0f, 0.05f);
            }
            HandleSlider_BGM_VOLUME.KeyHandlerInput();
            BGM_VOLUME = HandleSlider_BGM_VOLUME.getPosition();
        }
        font.draw(game.batch, "BGM Volume - " + (int) (BGM_VOLUME * 100) + "%", Box_Pause_X + Text_Pause_WIDTH / 8, Box_Pause_Y + Text_Pause_HEIGHT * 9);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 1) {
            font.setColor(Color.YELLOW);
            if(HandleSlider_SE_VOLUME == null) {
                HandleSlider_SE_VOLUME = new KeyHandler("x", SE_VOLUME, 1.0f, 0.05f);
            }
            HandleSlider_SE_VOLUME.KeyHandlerInput();
            SE_VOLUME = HandleSlider_SE_VOLUME.getPosition();
        }
        font.draw(game.batch, "SE Volume - " + (int) (SE_VOLUME * 100) + "%", Box_Pause_X + Text_Pause_WIDTH / 8, Box_Pause_Y + Text_Pause_HEIGHT * 8);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 2) {
            font.setColor(Color.YELLOW);
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                SAFE_MODE = !SAFE_MODE;
            }

        }
        font.draw(game.batch, "SAFE MODE - " + (SAFE_MODE ? "Yes": "NO!"), Box_Pause_X + Text_Pause_WIDTH / 8, Box_Pause_Y + Text_Pause_HEIGHT * 7);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 3) {
            font.setColor(Color.YELLOW);
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                OdysseyGame.BGM_VOLUME = BGM_VOLUME;
                OdysseyGame.SE_VOLUME = SE_VOLUME;
                OdysseyGame.SAFE_MODE = SAFE_MODE;
                OdysseyGame.settings.setSettings();
                option_success.play(OdysseyGame.SE_VOLUME);
            }
        }
        font.draw(game.batch, "  SAVE", Box_Pause_X + Text_Pause_WIDTH / 2, Box_Pause_Y + Text_Pause_HEIGHT * 5);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 4) {
            font.setColor(Color.YELLOW);
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.setScreen(this.menu);
                CloseSettings();
                option_success.play(OdysseyGame.SE_VOLUME);
            }
        }
        font.draw(game.batch, "Return to Title", Box_Pause_X + Text_Pause_WIDTH / 4, Box_Pause_Y + Text_Pause_HEIGHT * 3);

        font.setColor(Color.WHITE);
        if(HandleSettingStatus.getPosition() == 5) {
            font.setColor(Color.YELLOW);
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Gdx.app.exit();
                option_success.play(OdysseyGame.SE_VOLUME);
            }
        }
        font.draw(game.batch, "Quit Game", Box_Pause_X + Text_Pause_WIDTH / 2, Box_Pause_Y + Text_Pause_HEIGHT * 2);

        game.batch.end();
    }
    public void CloseSettings() {
        IsOpen = false;
    }

    public boolean IsOpen() {
        return IsOpen;
    }
}