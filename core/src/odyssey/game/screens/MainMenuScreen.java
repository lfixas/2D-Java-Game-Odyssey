package odyssey.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import odyssey.game.OdysseyGame;
import odyssey.game.Settings;
import odyssey.game.entity.PlayerData.PlayerData;
import odyssey.game.windows.MainSettingsWindows;


public class MainMenuScreen implements Screen {

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    private static final float TitleWidth = 316 * 1.8f;
    private static final float TitleHeight = 80 * 1.8f;

    private static final float Position_Y = OdysseyGame.HEIGHT / 50;
    private static final float ButtonPosition_X = OdysseyGame.WIDTH / 50;
    private static final ShapeRenderer shape = new ShapeRenderer();
    private static int buttonSelected_Play = 0;
    private static int buttonSelected_Settings = 0;
    private static int buttonSelected_Quit = 0;

    private MainSettingsWindows SettingsWindows = null;

    private static Music bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu1.ogg"));

    OdysseyGame game;

    PlayerData playerData = null;
    Texture title;
    Texture background;
    Color ButtonColor = new Color(0.188f, 0.188f, 0.188f, 1f);

    MainGameScreen gameScreen = null;

    public MainMenuScreen(OdysseyGame game) {
        this.game = game;
        title = new Texture("ui/main/title.png");
        background = new Texture("ui/main/background.jpg");
    }

    @Override
    public void show() {
        buttonSelected_Play = 0;
        buttonSelected_Settings = 0;
        buttonSelected_Quit = 0;
        bgm.setLooping(true);
        bgm.setVolume(OdysseyGame.BGM_VOLUME);
        bgm.play();

        if(playerData != null) {
            this.playerData = playerData;
        } else {
            playerData = new PlayerData();
        }
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        game.batch.begin();

        // BACKGROUND
        game.batch.draw(background, 0, 0, OdysseyGame.WIDTH, OdysseyGame.HEIGHT);

        // TITLE
        float x_Title = OdysseyGame.WIDTH / 2 - TitleWidth / 2 + OdysseyGame.WIDTH / 10;
        float y_Title = OdysseyGame.HEIGHT / 2 - TitleHeight / 2;
        game.batch.draw(title, x_Title, y_Title, TitleWidth, TitleHeight);
        game.batch.end();

        // PLAY

        if (Gdx.input.getX() < OdysseyGame.WIDTH / 4.3f + buttonSelected_Play + 5 && Gdx.input.getX() > 0 && OdysseyGame.HEIGHT - Gdx.input.getY() < Position_Y * 4 + buttonSelected_Play / 3 + Position_Y * 27 + 5 && OdysseyGame.HEIGHT - Gdx.input.getY() > Position_Y * 27 + buttonSelected_Play / 3) {
            buttonSelected_Play = 12;
            if (Gdx.input.isTouched()) {
                startGame();
            }
        } else {
            buttonSelected_Play = 0;
        }
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        shape.rect(0, Position_Y * 27, OdysseyGame.WIDTH / 4.3f + buttonSelected_Play, Position_Y * 4 + buttonSelected_Play / 3);
        shape.setColor(Color.WHITE);
        shape.rect(5, Position_Y * 27 + 5, OdysseyGame.WIDTH / 4.3f + buttonSelected_Play, Position_Y * 4 + buttonSelected_Play / 3);
        shape.end();
        game.batch.begin();
        font.setColor(ButtonColor);
        font.draw(game.batch, "PLAY", ButtonPosition_X + buttonSelected_Play, Position_Y * (27 + 3) + buttonSelected_Play / 3);
        game.batch.end();

        // SETTINGS
        if (Gdx.input.getX() < OdysseyGame.WIDTH / 4.3f + buttonSelected_Settings + 5 && Gdx.input.getX() > 0 && OdysseyGame.HEIGHT - Gdx.input.getY() < Position_Y * 4 + buttonSelected_Settings / 3 + Position_Y * (27 - 5) + 5 && OdysseyGame.HEIGHT - Gdx.input.getY() > Position_Y * (27 - 5) + buttonSelected_Settings / 3) {
            buttonSelected_Settings = 12;
            if (Gdx.input.isTouched()) {
                System.out.println("settings");
            }
        } else {
            buttonSelected_Settings = 0;
        }
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        shape.rect(0, Position_Y * (27 - 5), OdysseyGame.WIDTH / 4.3f + buttonSelected_Settings, Position_Y * 4 + buttonSelected_Settings / 3);
        shape.setColor(Color.WHITE);
        shape.rect(5, Position_Y * (27 - 5) + 5, OdysseyGame.WIDTH / 4.3f + buttonSelected_Settings, Position_Y * 4 + buttonSelected_Settings / 3);
        shape.end();
        game.batch.begin();
        font.setColor(ButtonColor);
        font.draw(game.batch, "SETTINGS", ButtonPosition_X + buttonSelected_Settings, Position_Y * (27 + 3 - 5) + buttonSelected_Settings / 3);
        game.batch.end();

        // QUIT
        if (Gdx.input.getX() < OdysseyGame.WIDTH / 4.3f + buttonSelected_Quit + 5 && Gdx.input.getX() > 0 && OdysseyGame.HEIGHT - Gdx.input.getY() < Position_Y * 4 + buttonSelected_Quit / 3 + Position_Y * (27 - 5 * 2) + 5 && OdysseyGame.HEIGHT - Gdx.input.getY() > Position_Y * (27 - 5 * 2) + buttonSelected_Quit / 3) {
            buttonSelected_Quit = 12;
            if (Gdx.input.isTouched()) {
                quitGame();
            }
        } else {
            buttonSelected_Quit = 0;
        }
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        shape.rect(0, Position_Y * (27 - 5 * 2), OdysseyGame.WIDTH / 4.3f + buttonSelected_Quit, Position_Y * 4 + buttonSelected_Quit / 3);
        shape.setColor(Color.WHITE);
        shape.rect(5, Position_Y * (27 - 5 * 2) + 5, OdysseyGame.WIDTH / 4.3f + buttonSelected_Quit, Position_Y * 4 + buttonSelected_Quit / 3);
        shape.end();
        game.batch.begin();
        font.setColor(ButtonColor);
        font.draw(game.batch, "QUIT", ButtonPosition_X + buttonSelected_Quit, Position_Y * (27 + 3 - 5 * 2) + buttonSelected_Quit / 3);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void startGame() {
        if (this.gameScreen == null) {
            this.gameScreen = new MainGameScreen(this.playerData, game, this);
        }
        game.setScreen(this.gameScreen);
        bgm.dispose();
    }

    public void quitGame() {
        Gdx.app.exit();
    }
}
