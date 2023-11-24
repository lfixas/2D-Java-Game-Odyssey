package odyssey.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import odyssey.game.KeyMenu.KeyHandler;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.Enemy.Enemy;
import odyssey.game.entity.PlayerData.Crew;
import odyssey.game.entity.PlayerData.PlayerData;
import odyssey.game.entity.player.Player;

import java.io.IOException;
import java.lang.management.BufferPoolMXBean;
import java.util.Random;

public class MainFightScreen implements Screen {

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    OdysseyGame game;
    private MainGameScreen gameScreen = null;

    private MainMenuScreen menu;

    private final PlayerData playerData;

    private Crew Crew0;

    private Crew Crew1;

    private Crew Crew2;

    private ShapeRenderer shape = new ShapeRenderer();
    private int BottomOffset = 0;

    private float timeSeconds = 0f;
    private float period = 0.1f;

    private boolean keyPressed = false;
    private float keyPressedTime = 0f;

    private int frame = 0;
    private int state = 0;

    private String fullText = "A heavy atmosphere arises!";
    private String currentText = "";

    private String fightOption = "";

    private Crew frontLine;

    private int index = 0;

    private Enemy enemy;

    private Texture sprite;
    private int spriteShake;
    private boolean spriteAppears;

    private float RelativeSpeed = 0.5f;

    private int StunEnemy = 0;

    private int DamageBuff = 0;
    private int damageDeal = 0;
    private static Music bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds/fight1.ogg"));

    private final KeyHandler HandleFightOption = new KeyHandler("x", 0, 3, 1);

    private final KeyHandler HandleFightFight = new KeyHandler("y", 0, 2, 1);

    private final KeyHandler HandleFightAct = new KeyHandler("y", 0, 1, 1);

    private KeyHandler HandleFightSwitch = null;

    public MainFightScreen(PlayerData playerData, OdysseyGame game, MainMenuScreen menu, MainGameScreen gameScreen, Enemy enemy) {
        this.playerData = playerData;
        this.game = game;
        this.menu = menu;
        this.gameScreen = gameScreen;
        this.enemy = enemy;
    }

    @Override
    public void show() {
        BottomOffset = 0;
        fullText = "A heavy atmosphere arises!";
        index = 0;
        timeSeconds = 0;
        frame = 0;
        state = 0;
        keyPressedTime = 0f;
        fightOption = "";
        sprite = enemy.getSprite();
        spriteShake = 0;
        spriteAppears = false;
        StunEnemy = 0;
        DamageBuff = 0;
        damageDeal = 0;
        Crew0 = new Crew(playerData.getName(), playerData.getMaxLife(), playerData.getLife(), playerData.getDamage(), playerData.getAttack1(), playerData.getAttack2(), playerData.getAttack3());
        Crew1 = gameScreen.getCrew1();
        Crew2 = gameScreen.getCrew2();
        frontLine = Crew0;
        bgm.setLooping(true);
        bgm.setVolume(OdysseyGame.BGM_VOLUME);
        bgm.play();
    }

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }

        else if (operatingSystem.contains("Windows")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }

    @Override
    public void render(float delta) {

        // Check Enemy Death
        if(enemy.getLife() <= 0 && state < 10000) {
            state = 9999;
        }

        // Check for Death
        if(Crew0.getLife() <= 0) {
            if(OdysseyGame.SAFE_MODE) {
                Gdx.app.exit();
            } else {
                try {
                    shutdown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(Crew1 != null && Crew1.getLife() <= 0) {
            this.Crew1 = null;
            frontLine = Crew0;
        }
        if(Crew2 != null && Crew2.getLife() <= 0) {
            this.Crew2 = null;
            frontLine = Crew0;
        }

        // Check Enemy Speed
        if(playerData.getSpeed() > enemy.getSpeed()) {
            RelativeSpeed = 0.7f;
        } else {
            RelativeSpeed = 0.3f;
        }

        // Timer
        timeSeconds += Gdx.graphics.getDeltaTime();
        if(timeSeconds > period) {
            timeSeconds -= period;
            if (index < fullText.length()) {
                currentText = fullText.substring(0, index + 1);
                index++;
            } else if (index == fullText.length()) {
                state++;
                index++;
            }
            frame++;
        }

        // Change Text
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && index > fullText.length() || Gdx.input.isTouched() && index > fullText.length()) {
            State();
        }

        // Quick Text
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
            keyPressedTime += Gdx.graphics.getDeltaTime();

            if (keyPressedTime >= 0.42f && index < fullText.length()) {
                index = fullText.length() - 1;
                keyPressedTime = 0f;
            }
        } else {
            keyPressed = false;
            keyPressedTime = 0f;
        }

        ScreenUtils.clear(0f, 0f, 0f, 0.8f);

        Enemy();

        Background();

        PlayerUI();

        // FIGHT
        if(BottomOffset != 0) {

            HandleFightOption.KeyHandlerInput();

            Fight();
            Act();
            Items();
            Flee();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ENTER)) {
            exitFight();
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

    public void Background() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);
        shape.rect( 20, 20 + BottomOffset, OdysseyGame.WIDTH - 40, OdysseyGame.HEIGHT / 3);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect( 30, 30 + BottomOffset, OdysseyGame.WIDTH - 60, OdysseyGame.HEIGHT / 3 - 20);
        shape.end();

        game.batch.begin();
        font.setColor(Color.WHITE);
        font.draw(game.batch, currentText, 60, OdysseyGame.HEIGHT / 3 - 20 + BottomOffset);
        game.batch.end();
    }

    public void Enemy() {
        if(spriteAppears) {
            game.batch.begin();
            if (BottomOffset == 0) {
                if (spriteShake == 0) {
                    game.batch.draw(sprite, OdysseyGame.WIDTH / 5, OdysseyGame.HEIGHT / 3, 448, 448);
                } else {
                    if (frame % 2 == 0) {
                        game.batch.draw(sprite, OdysseyGame.WIDTH / 5 - 20, OdysseyGame.HEIGHT / 3, 448, 448);
                        spriteShake--;
                    } else {
                        game.batch.draw(sprite, OdysseyGame.WIDTH / 5 + 20, OdysseyGame.HEIGHT / 3, 448, 448);
                    }
                }
            } else {
                if (spriteShake == 0) {
                    game.batch.draw(sprite, OdysseyGame.WIDTH / 4.5f, OdysseyGame.HEIGHT / 2, 320, 320);
                } else {
                    if (frame % 2 == 0) {
                        game.batch.draw(sprite, OdysseyGame.WIDTH / 4.5f - 20, OdysseyGame.HEIGHT / 2, 320, 320);
                        spriteShake--;
                    } else {
                        game.batch.draw(sprite, OdysseyGame.WIDTH / 4.5f + 20, OdysseyGame.HEIGHT / 2, 320, 320);
                    }
                }
            }
            game.batch.end();
        } else {
            spriteShake = 20;
        }
    }

    public void PlayerUI() {
        float PlayerLife = 1 - (float) (Crew0.getMaxLife() - Crew0.getLife()) / Crew0.getMaxLife();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.GRAY);
        shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40, 40);

        if(PlayerLife >= 0.75f) {
            shape.setColor(Color.GREEN);
        } if(PlayerLife < 0.75f) {
            shape.setColor(Color.YELLOW);
        } if(PlayerLife <= 0.25f) {
            shape.setColor(Color.RED);
        }
        shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset, (OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40) * PlayerLife, 40);
        shape.end();

        game.batch.begin();
        if(frontLine == Crew0) {
            font.setColor(Color.CYAN);
        } else {
            font.setColor(Color.WHITE);
        }
        font.draw(game.batch, playerData.getName(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35 + 40, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40, 1, true);
        font.setColor(Color.WHITE);
        font.draw(game.batch, Crew0.getLife() + " / " + Crew0.getMaxLife(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40, 1, true);
        game.batch.end();

        if(Crew1 != null) {
            float Crew1Life = 1 - (float) (Crew1.getMaxLife() - Crew1.getLife()) / Crew1.getMaxLife();

            shape.begin(ShapeRenderer.ShapeType.Filled);

            shape.setColor(Color.GRAY);
            shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 100, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 30);

            if(Crew1Life >= 0.75f) {
                shape.setColor(Color.GREEN);
            } if(Crew1Life < 0.75f) {
                shape.setColor(Color.YELLOW);
            } if(Crew1Life <= 0.25f) {
                shape.setColor(Color.RED);
            }
            shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 100, (OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100) * Crew1Life, 30);
            shape.end();

            game.batch.begin();
            font.getData().setScale(0.75f);
            if(frontLine == Crew1) {
                font.setColor(Color.CYAN);
            } else {
                font.setColor(Color.WHITE);
            }
            font.draw(game.batch, Crew1.getName(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35 + 40 - 10 + 100, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 1, true);
            font.setColor(Color.WHITE);
            font.draw(game.batch, Crew1.getLife() + " / " + Crew1.getMaxLife(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35 - 10 + 100, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 1, true);
            font.getData().setScale(1.0f);
            game.batch.end();
        }

        if(Crew2 != null) {
            float Crew2Life = 1 - (float) (Crew2.getMaxLife() - Crew2.getLife()) / Crew2.getMaxLife();

            shape.begin(ShapeRenderer.ShapeType.Filled);

            shape.setColor(Color.GRAY);
            shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 100 * 2, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 30);

            if(Crew2Life >= 0.75f) {
                shape.setColor(Color.GREEN);
            } if(Crew2Life < 0.75f) {
                shape.setColor(Color.YELLOW);
            } if(Crew2Life <= 0.25f) {
                shape.setColor(Color.RED);
            }
            shape.rect( OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 100 * 2, (OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100) * Crew2Life, 30);
            shape.end();

            game.batch.begin();
            font.getData().setScale(0.75f);
            if(frontLine == Crew2) {
                font.setColor(Color.CYAN);
            } else {
                font.setColor(Color.WHITE);
            }
            font.draw(game.batch, Crew2.getName(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35 + 40 - 10 + 100 * 2, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 1, true);
            font.setColor(Color.WHITE);
            font.draw(game.batch, Crew2.getLife() + " / " + Crew2.getMaxLife(), OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 + 100, OdysseyGame.HEIGHT / 3 + 40 + BottomOffset + 35 - 10 + 100 * 2, OdysseyGame.WIDTH - OdysseyGame.WIDTH / 2 - 40 - 100, 1, true);
            font.getData().setScale(1.0f);
            game.batch.end();
        }
    }

    public void State() {
        switch (state) {
            case 1:
                if (enemy != null) {
                    fullText = "An enemy appears..\nIt's a " + enemy.getName() + "!";
                } else {
                    fullText = "An enemy appears..";
                }
                index = 0;
                spriteAppears = true;
                break;
            case 2:
                fullText = "What do you do?";
                index = 0;
                break;
            case 3:
                BottomOffset = 140;
                break;
            case 100:
                fullText = "";
                fightOption = "fight";
                index = -1;
                BottomOffset = 140;
                break;
            case 200:
                damageDeal = (frontLine.getAttack1().getDamage() + DamageBuff) * (int) frontLine.getDamage() ;
                enemy.setLife(enemy.getLife() - damageDeal);
                fullText = String.format(frontLine.getAttack1().getLore(),enemy.getName(), damageDeal);
                if(frontLine.getAttack1().getProperty().equals("Stun")) {
                    StunEnemy = 3;
                }
                if(frontLine.getAttack1().getProperty().equals("DamageBuff")) {
                    DamageBuff++;
                }
                index = 0;
                fightOption = "";
                spriteShake = 20;
                BottomOffset = 0;
                state = 999;
                break;
            case 210:
                damageDeal = (frontLine.getAttack2().getDamage() + DamageBuff) * (int) frontLine.getDamage() ;
                enemy.setLife(enemy.getLife() - damageDeal);
                fullText = String.format(frontLine.getAttack2().getLore(),enemy.getName(), damageDeal);
                if(frontLine.getAttack2().getProperty().equals("Stun")) {
                    StunEnemy = 3;
                }
                if(frontLine.getAttack2().getProperty().equals("DamageBuff")) {
                    DamageBuff++;
                }
                index = 0;
                fightOption = "";
                spriteShake = 20;
                BottomOffset = 0;
                state = 999;
                break;
            case 220:
                damageDeal = (frontLine.getAttack3().getDamage() + DamageBuff) * (int) frontLine.getDamage() ;
                enemy.setLife(enemy.getLife() - damageDeal);
                fullText = String.format(frontLine.getAttack3().getLore(),enemy.getName(), damageDeal);
                if(frontLine.getAttack3().getProperty().equals("Stun")) {
                    StunEnemy = 3;
                }
                if(frontLine.getAttack3().getProperty().equals("DamageBuff")) {
                    DamageBuff++;
                }
                index = 0;
                fightOption = "";
                spriteShake = 20;
                BottomOffset = 0;
                state = 999;
                break;
            case 300:
                fullText = "";
                fightOption = "act";
                index = -1;
                BottomOffset = 140;
                break;
            case 310:
                fullText = "You scan the " + enemy.getName() + "..";
                index = 0;
                fightOption = "";
                BottomOffset = 0;
                break;
            case 311:
                if(stunStatusEnemy()) {
                    fullText = "* " + enemy.getName() + "..\n* Status: Stunned!\n* Life: " + enemy.getLife() + " / " + enemy.getMaxLife() + "\n* Damage: " + enemy.getDamage();
                } else {
                    fullText = "* " + enemy.getName() + "..\n* Status: Hostile?\n* Life: " + enemy.getLife() + " / " + enemy.getMaxLife() + "\n* Damage: " + enemy.getDamage();
                }
                index = 0;
                state = 999;
                break;
            case 400:
                fullText = "* No Items";
                fightOption = "";
                index = fullText.length() - 1;
                state = 3;
                break;
            case 500:
                fullText = "You try to flee..";
                fightOption = "";
                index = 0;
                BottomOffset = 0;
                break;
            case 501:
                if(fleeChance(RelativeSpeed)) {
                    fullText = "The " + enemy.getName() + " watch you run away..";
                } else {
                    fullText = "The " + enemy.getName() + " is chasing you..";
                    state = 510;
                }
                index = 0;
                BottomOffset = 0;
                break;
            case 502:
                fullText = "You flee with success!";
                index = 0;
                break;
            case 503:
                exitFight();
                break;
            case 511:
                fullText = "You didn't manage to escape\nthe " + enemy.getName() + "..";
                index = 0;
                state = 999;
                break;
            case 600:
                fullText = "";
                fightOption = "switch";
                index = -1;
                BottomOffset = 140;
                break;
            case 610:
                fullText = "You switch the front line to\n" + Crew0.getName();
                fightOption = "";
                frontLine = Crew0;
                index = 0;
                BottomOffset = 0;
                state = 999;
                break;
            case 620:
                fullText = "You switch the front line to\n" + Crew1.getName();
                fightOption = "";
                frontLine = Crew1;
                index = 0;
                BottomOffset = 0;
                state = 999;
                break;
            case 630:
                fullText = "You switch the front line to\n" + Crew2.getName();
                fightOption = "";
                frontLine = Crew2;
                index = 0;
                BottomOffset = 0;
                state = 999;
                break;
            case 650:
                fullText = "The front line is already to\n" + frontLine.getName();
                fightOption = "";
                index = 0;
                BottomOffset = 140;
                state = 599;
                break;
            case 1000:
                fullText = "The " + enemy.getName() + " attack you..";
                index = 0;
                break;
            case 1001:
                if (stunStatusEnemy()) {
                    StunEnemy--;
                    int damage = (enemy.getDamage() - 5) < 0 ? 0 : (enemy.getDamage() - 5);
                    frontLine.setLife(frontLine.getLife() - damage);
                    fullText = "The " + enemy.getName() + " is stunned!\nYou take " + damage + " damage!";
                } else {
                    frontLine.setLife(frontLine.getLife() - enemy.getDamage());
                    fullText = "You take " + enemy.getDamage() + " damage!";
                }
                index = 0;
                state = 2;
                break;
            case 10000:
                fullText = "The " + enemy.getName() + " dissolve itself..";
                index = 0;
                spriteShake = 20;
                break;
            case 10001:
                fullText = "You win!";
                index = 0;
                spriteAppears = false;
                break;
            case 10002:
                exitFight();
                break;
        }
        keyPressed = true;
    }

    public void Fight() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.ORANGE);
        shape.rect( 20, 40, 280, BottomOffset - 40);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect( 30, 50, 260, BottomOffset - 60);
        shape.end();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 0) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.1f);
            font.draw(game.batch, "FIGHT!", 60 - 5, BottomOffset - 40 + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 100;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "FIGHT!", 60, BottomOffset - 40);
        }
        game.batch.end();

        if(fightOption == "fight") {
            FightOptionFight();
        }
    }

    public void Act() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.ORANGE);
        shape.rect(20 + 280 + 40, 40, 280, BottomOffset - 40);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect(30 + 280 + 40, 50, 260, BottomOffset - 60);
        shape.end();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 1) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.1f);
            font.draw(game.batch, "ACT", 60 - 5 + 280 + 40 + 20 + 10 + 20, BottomOffset - 40 + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 300;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "ACT", 60 + 280 + 40 + 20 + 10 + 20, BottomOffset - 40);
        }
        game.batch.end();

        if(fightOption == "act") {
            FightOptionAct();
        }

        if(fightOption == "switch") {
            FightOptionSwitch();
        }
    }

    public void Items() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.ORANGE);
        shape.rect(20 + (280 + 40) * 2, 40, 280, BottomOffset - 40);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect(30 + (280 + 40) * 2, 50, 260, BottomOffset - 60);
        shape.end();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 2) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.1f);
            font.draw(game.batch, "ITEMS", 60 - 5 + (280 + 40) * 2 + 20, BottomOffset - 40 + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 400;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "ITEMS", 60 + (280 + 40) * 2 + 20, BottomOffset - 40);
        }
        game.batch.end();
    }

    public void Flee() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.ORANGE);
        shape.rect(20 + (280 + 40) * 3, 40, 280, BottomOffset - 40);

        shape.setColor(new Color(0.12f, 0.12f, 0.15f, 1));
        shape.rect(30 + (280 + 40) * 3, 50, 260, BottomOffset - 60);
        shape.end();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 3) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.1f);
            font.draw(game.batch, "FLEE", 60 - 5 + (280 + 40) * 3 + 20 + 10, BottomOffset - 40 + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 500;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "FLEE", 60 + (280 + 40) * 3 + 20 + 10, BottomOffset - 40);
        }
        game.batch.end();
    }

    public void FightOptionFight() {
        HandleFightFight.KeyHandlerInput();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 0 && HandleFightFight.getPosition() == 0) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "* " + frontLine.getAttack1().getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 5 + BottomOffset + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 200;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "* " + frontLine.getAttack1().getName(), 60, OdysseyGame.HEIGHT / 3 - 20 + BottomOffset);
        }

        if (HandleFightOption.getPosition() == 0 && HandleFightFight.getPosition() == 1) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "* " + frontLine.getAttack2().getName(), 60 - 5, OdysseyGame.HEIGHT / 3 - 20 - 60 + 5 + BottomOffset);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 210;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "* " + frontLine.getAttack2().getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 60 + BottomOffset);
        }

        if (HandleFightOption.getPosition() == 0 && HandleFightFight.getPosition() == 2) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "* " + frontLine.getAttack3().getName(), 60 - 5, OdysseyGame.HEIGHT / 3 - 20 - 60 * 2 + 5 + BottomOffset);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 220;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "* " + frontLine.getAttack3().getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 60 * 2 + BottomOffset);
        }
        game.batch.end();
    }

    public void FightOptionAct() {
        HandleFightAct.KeyHandlerInput();

        game.batch.begin();
        if (HandleFightOption.getPosition() == 1 && HandleFightAct.getPosition() == 0) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "* Scan the Enemy", 60, OdysseyGame.HEIGHT / 3 - 20 - 5 + BottomOffset + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 310;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "* Scan the Enemy", 60, OdysseyGame.HEIGHT / 3 - 20 + BottomOffset);
        }
        // SWITCH
        if (HandleFightOption.getPosition() == 1 && HandleFightAct.getPosition() == 1) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "Switch Front line", 60 - 5, OdysseyGame.HEIGHT / 3 - 20 - 60 + 5 + BottomOffset);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 600;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "Switch Front line", 60, OdysseyGame.HEIGHT / 3 - 20 - 60 + BottomOffset);
        }
        game.batch.end();
    }

    public void FightOptionSwitch() {
        if(HandleFightSwitch == null) {
            HandleFightSwitch = new KeyHandler("y", 0, 2, 1);
        }

        HandleFightSwitch.KeyHandlerInput();

        game.batch.begin();
        // SWITCH PLAYER
        if (HandleFightOption.getPosition() == 1 && HandleFightSwitch.getPosition() == 0) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(1.05f);
            font.draw(game.batch, "* Switch to " + Crew0.getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 5 + BottomOffset + 5);
            font.getData().setScale(1.0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && frontLine == Crew0) {
                state = 650;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                state = 610;
            }
        } else {
            font.setColor(Color.WHITE);
            font.draw(game.batch, "* Switch to " + Crew0.getName(), 60, OdysseyGame.HEIGHT / 3 - 20 + BottomOffset);
        }
        // SWITCH CREW1
        if (Crew1 != null) {
            if (HandleFightOption.getPosition() == 1 && HandleFightSwitch.getPosition() == 1) {
                font.setColor(Color.YELLOW);
                font.getData().setScale(1.05f);
                font.draw(game.batch, "* Switch to " + Crew1.getName(), 60 - 5, OdysseyGame.HEIGHT / 3 - 20 - 60 + 5 + BottomOffset);
                font.getData().setScale(1.0f);
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && frontLine == Crew1) {
                    state = 650;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    state = 620;
                }
            } else {
                font.setColor(Color.WHITE);
                font.draw(game.batch, "* Switch to " + Crew1.getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 60 + BottomOffset);
            }
        }
        // SWITCH CREW2
        if (Crew2 != null) {
            if (HandleFightOption.getPosition() == 1 && HandleFightSwitch.getPosition() == 2) {
                font.setColor(Color.YELLOW);
                font.getData().setScale(1.05f);
                font.draw(game.batch, "* Switch to " + Crew2.getName(), 60 - 5, OdysseyGame.HEIGHT / 3 - 20 - 60 * 2 + 5 + BottomOffset);
                font.getData().setScale(1.0f);
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && frontLine == Crew2) {
                    state = 650;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    state = 630;
                }
            } else {
                font.setColor(Color.WHITE);
                font.draw(game.batch, "* Switch to " + Crew2.getName(), 60, OdysseyGame.HEIGHT / 3 - 20 - 60 * 2 + BottomOffset);
            }
            game.batch.end();
        }
    }

    public void exitFight() {
        playerData.setLife(Crew0.getLife());
        gameScreen.setCrew1(Crew1);
        gameScreen.setCrew2(Crew2);

        if (this.gameScreen == null) {
            this.gameScreen = new MainGameScreen(playerData, game, menu);
        }

        bgm.dispose();
        this.gameScreen.dispose();
        game.setScreen(this.gameScreen);

    }

    public boolean fleeChance(double probability) {
        Random random = new Random();
        return random.nextDouble() <= probability;
    }

    public boolean stunStatusEnemy() {
        if(StunEnemy != 0) {
            return true;
        } else {
            return false;
        }
    }
}
