package odyssey.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import odyssey.game.Infos.*;
import odyssey.game.KeyMenu.KeyHandler;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.PlayerData.PlayerData;

public class MainTravelScreen implements Screen {

    private PlayerData playerData;

    private OdysseyGame game;

    private MainMenuScreen menu;

    private MainGameScreen main;

    private KeyHandler HandleSelectedPosition;

    private final Planet_1 planet1 = new Planet_1();
    private final Planet_2 planet2 = new Planet_2();
    private final Planet_3 planet3 = new Planet_3();
    private final Planet_4 planet4 = new Planet_4();
    private final Planet_5 planet5 = new Planet_5();
    private final Planet_6 planet6 = new Planet_6();
    private final Planet_7 planet7 = new Planet_7();
    private final Planet_null planetNull = new Planet_null();

    private InfoPlanets planet;

    private final Texture screen = new Texture("ui/travel/screen.png");
    private final Texture travel_path = new Texture("ui/travel/travel_path.png");
    private final Texture travel_map_objectif_icon = new Texture("ui/travel/travel_map_objectif_icon.png");
    private final Texture travel_map_position_icon = new Texture("ui/travel/travel_map_position_icon.png");
    private final Texture travel_map_unlock_icon = new Texture("ui/travel/travel_map_unlock_icon.png");
    private final Texture travel_map_lock_icon = new Texture("ui/travel/travel_map_lock_icon.png");

    float ScreenWidth = OdysseyGame.HEIGHT;
    float ScreenHeight = OdysseyGame.HEIGHT;
    float x_Screen = OdysseyGame.WIDTH / 2 - ScreenWidth / 2;
    float y_Screen = OdysseyGame.HEIGHT / 2 - ScreenHeight / 2;
    float Objectifs = ScreenWidth / 6;

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    private static final ShapeRenderer shape = new ShapeRenderer();

    public MainTravelScreen(PlayerData playerData, OdysseyGame game, MainMenuScreen menu, MainGameScreen main) {
        this.playerData = playerData;
        this.game = game;
        this.menu = menu;
        this.main = main;
    }
    @Override
    public void show()
    {
        playerData.updateUnlocked();
        HandleSelectedPosition = new KeyHandler("x", 0, 7, 1);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        HandleSelectedPosition.KeyHandlerInput();

        game.batch.begin();

        // Screen //
        game.batch.draw(screen, x_Screen, y_Screen, ScreenWidth, ScreenHeight);
        // path
        game.batch.draw(travel_path, x_Screen, y_Screen, ScreenWidth, ScreenHeight);

        // Objectifs
        if(HandleSelectedPosition.getPosition() > 5) {
            travelUnlocked(6);
        } else {
            travelUnlocked(1);
        }

        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && playerData.getUnlocked() >= planet.getID()) {
            System.out.println(this.planet.getID());

            if (this.planet.getID() > playerData.getUnlocked())
                return;

            playerData.setPlanetLocation(planet.getID());
            this.game.setScreen(this.main);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(this.main);
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    public void travelUnlocked(int start) {
        int position = 1;
        for(int i = start; i < start + 5; i++) {
            if(i > 7) {
                break;
            }

            if(i <= playerData.getUnlocked()) {
                game.batch.draw(travel_map_unlock_icon, x_Screen + Objectifs * position, y_Screen + ScreenHeight / 2.2f, travel_map_objectif_icon.getWidth() * 4, travel_map_objectif_icon.getHeight() * 4);
            } else {
                game.batch.draw(travel_map_lock_icon, x_Screen + Objectifs * position, y_Screen + ScreenHeight / 2.15f, travel_map_objectif_icon.getWidth() * 4, travel_map_objectif_icon.getHeight() * 4);
            }

            if(HandleSelectedPosition.getPosition() == i) {
                game.batch.draw(travel_map_objectif_icon, x_Screen + Objectifs * position + 2, y_Screen + ScreenHeight / 2.1f, travel_map_objectif_icon.getWidth() * 4, travel_map_objectif_icon.getHeight() * 4);
            }

            if(i == playerData.getPlanetLocation()) {
                game.batch.draw(travel_map_position_icon, x_Screen + Objectifs * position, y_Screen + ScreenHeight / 2.2f, travel_map_objectif_icon.getWidth() * 4, travel_map_objectif_icon.getHeight() * 4);
            }
            switch((int) HandleSelectedPosition.getPosition()) {
                case 1:
                    planet = planet1;
                    break;
                case 2:
                    planet = planet2;
                    break;
                case 3:
                    planet = planet3;
                    break;
                case 4:
                    planet = planet4;
                    break;
                case 5:
                    planet = planet5;
                    break;
                case 6:
                    planet = planet6;
                    break;
                case 7:
                    planet = planet7;
                    break;
                default:
                    planet = planetNull;
            }

            font.getData().setScale(0.5f);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "Planet:", x_Screen + ScreenWidth / 10, ScreenHeight / 1.1f, ScreenWidth * 0.8f, 1, true);
            font.setColor(Color.YELLOW);
            font.draw(game.batch, "\n" + planet.getName(), x_Screen + ScreenWidth / 10, ScreenHeight / 1.1f, ScreenWidth * 0.8f, 1, true);
            font.setColor(Color.WHITE);
            font.draw(game.batch, "\n\nEnvironment:", x_Screen + ScreenWidth / 10, ScreenHeight / 1.2f, ScreenWidth * 0.8f, 1, true);
            font.setColor(Color.YELLOW);
            font.draw(game.batch, "\n\n\n" + planet.getEnvironment(), x_Screen + ScreenWidth / 10, ScreenHeight / 1.2f, ScreenWidth * 0.8f, 1, true);

            font.setColor(Color.WHITE);
            font.draw(game.batch, "Description:", x_Screen + ScreenWidth / 10, ScreenHeight / 2.2f, ScreenWidth * 0.8f, 1, true);
            font.setColor(Color.YELLOW);
            font.draw(game.batch, "\n" + planet.getLore(), x_Screen + ScreenWidth / 10, ScreenHeight / 2.2f, ScreenWidth * 0.8f, 1, true);
            position++;
        }
    }
}
