package odyssey.game.screens;

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import jdk.tools.jmod.Main;
import odyssey.game.MapGenerator.CellularAutomaton;
import odyssey.game.entity.Attack.Attack;
import odyssey.game.entity.Attack.Insult;
import odyssey.game.entity.Attack.ThrowRock;
import odyssey.game.entity.Enemy.Bebou;
import odyssey.game.entity.Enemy.Enemy;
import odyssey.game.entity.Enemy.Slime;
import odyssey.game.entity.Enemy.Slug;
import com.badlogic.gdx.utils.viewport.Viewport;
import odyssey.game.entity.NPC.gumbot;
import odyssey.game.entity.PlayerData.Crew;
import odyssey.game.entity.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.PlayerData.PlayerData;
import odyssey.game.ui.InventoryUI;
import odyssey.game.ui.CraftUI;
import odyssey.game.ui.InventoryUI;
import odyssey.game.windows.ChatWindows;
import odyssey.game.windows.MainSettingsWindows;
import odyssey.game.windows.Tuto.Tuto;
import odyssey.game.windows.WarpToConfirm;

import java.util.Random;

public class MainGameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Stage stage = null;
    private FillViewport viewport = null;
    private float mapScale = 4.0f;
    OdysseyGame game;
    Player player;
    PlayerData playerData = null;
    private Crew Crew1 = null;
    private Crew Crew2 = null;
    private MainMenuScreen menu;
    private MainFightScreen fightScreen;
    private MainSettingsWindows SettingsWindows = null;
    private MainTravelScreen travel = null;
    private MainCraftScreen craftScreen = null;
    private InventoryUI invUI = null;
    private CraftUI craftUI = null;
    private gumbot gumbot = null;
    private Sprite sprite;
    private Tuto tuto = null;
    private ChatWindows chat = null;
    private float timeUntilToConfirm = 5f;
    private Vector2 playerStart = new Vector2(0, 0);

    private WarpToConfirm warpConfirm = null;
    public static boolean PAUSE = false;
    private static Music bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds/ship1.ogg"));
  
    public enum Screen {
        Main, Settings, Travel, Combat, Craft
    }

    public MainGameScreen(PlayerData playerData, OdysseyGame game, MainMenuScreen menu) {
        this.playerData = playerData;
        this.game = game;
        this.menu = menu;
        this.travel = new MainTravelScreen(playerData, game, menu, this);
        this.craftScreen = new MainCraftScreen(this);
    }
    @Override
    public void show() {
        bgm.setLooping(true);
        bgm.setVolume(OdysseyGame.BGM_VOLUME);
        bgm.play();
        resume();

        this.playerStart = new Vector2(-100, -100);

        if(tuto ==  null) {
            tuto = new Tuto(game);
        }

        if(chat == null) {
            chat = new ChatWindows(game, playerData, this);
        }

//        if(Crew1 == null) {
//            Crew1 = new Crew("R0b0TO3000-T2", 50, 30, 3, new ThrowRock(), new ThrowRock(), new Insult());
//        }
//
        if(Crew2 == null) {
            Crew2 = new Crew("El-CONQUISTADOR", 300, 120, 8, new ThrowRock(), new Insult(), new ThrowRock());
        }

        TmxMapLoader loader = new TmxMapLoader();
        switch (playerData.getPlanetLocation()) {
            case 1:
                this.map = loader.load("Maps/DebugMap.tmx");
                break;
            case 2:
                this.map = loader.load("Maps/AquaVeridia.tmx");
                break;
            case 3:
                this.map = loader.load("Maps/Cryo.tmx");
                break;
            case 4:
                this.map = loader.load("Maps/Inferno.tmx");
                break;
            case 5:
                this.map = loader.load("Maps/Stormshade.tmx");
                break;
            case 101:
                this.map = loader.load(System.getProperty("user.dir") + "/assets/Maps/GeneratedMap.tmx");
                setupGeneratedMap();
                break;
            default:
                this.map = loader.load("Maps/starship.tmx");
                break;
        }

        this.viewport = new FillViewport(OdysseyGame.WIDTH, OdysseyGame.HEIGHT);
        this.renderer = new OrthogonalTiledMapRenderer(this.map, this.mapScale);
        this.stage = new Stage(this.viewport);

        this.player = new Player(this.game, (OrthographicCamera) this.stage.getCamera(), this);

        this.stage.addActor(this.player);
        this.stage.setKeyboardFocus(this.player);

        this.invUI = new InventoryUI(this.stage, this.player.getInventory());
        // this.craftUI = new CraftUI(this.stage);

        timeUntilToConfirm = 5f;
    }

    public TiledMapTileLayer getMapLayer(String layerName) {
        assert this.map.getLayers().get(layerName) != null;

        return (TiledMapTileLayer) this.map.getLayers().get(layerName);
    }

    public Viewport getViewport() {
        return this.viewport;
    }
    public float getMapScale() {
        return this.mapScale;
    }
    public InventoryUI getInventoryUI() { return this.invUI; }
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            game.setScreen(this.menu);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            OpenSettings();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            fightStart();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            WarpToShip();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F3) && Gdx.input.isKeyPressed(Input.Keys.R)) {
            ReloadMap();
        }

        this.stage.act(delta);
        this.renderer.setView((OrthographicCamera) this.stage.getCamera());
        this.renderer.render();
        NPCLayer();
        this.stage.draw();
        ChatGumbot();

        if(timeUntilToConfirm < 6f) {
            timeUntilToConfirm += Gdx.graphics.getDeltaTime();
        }

        if(warpConfirm != null && warpConfirm.IsToConfirm() && timeUntilToConfirm > 5f) {
            pause();
            warpConfirm.ConfirmPopUp();
            if(warpConfirm.IsConfirmed() == "true") {
                playerData.setPlanetLocation(warpConfirm.getLocation());
                GenerateMap("Cave");
                ReloadMap();
                resume();
                warpConfirm.ToConfirm(false);
            } else if(warpConfirm.IsConfirmed() == "false") {
                resume();
                warpConfirm.ToConfirm(false);
                timeUntilToConfirm = 0f;
            }
        }

        CheckOpenSettings();
    }
    @Override
    public void resize(int width, int height){}

    @Override
    public void pause() {
        PAUSE = true;
    }

    @Override
    public void resume() {
        PAUSE = false;
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        bgm.stop();
    }

    private Vector2 getCenteredPlayerPos() {
        Vector2 vec = new Vector2(OdysseyGame.WIDTH / 2,  OdysseyGame.HEIGHT / 2);

        return this.getViewport().unproject(vec);
    }

    private void NPCLayer() {
        if(playerData.getPlanetLocation() == 0) {
            if (gumbot == null) {
                gumbot = new gumbot();
                sprite = new Sprite(gumbot.getSprite());
            }
            Vector2 player = getCenteredPlayerPos();
            float gumbotX = (1550 - player.x), gumbotY = (1500 - player.y);
            sprite.setPosition(gumbotX, gumbotY);

            game.batch.begin();
            sprite.draw(game.batch);
            game.batch.end();

            if (Vector2.dst(gumbotX, gumbotY, player.x - 400, player.y - 800) < 200) {
                tuto.pressE();
                if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !chat.IsChatting()) {
                    chat.Chat(true);
                    chat.startChat();
                }
            } else if (chat.IsChatting()) {
                chat.Chat(false);
            }
        }
    }

    public void ChatGumbot() {
        chat.ChatPopUp(gumbot.getChat(),gumbot.AddAsCrewMember());
    }

    public void fightStart() {
        this.fightScreen = new MainFightScreen(playerData, game, menu, this, generateCombat());
        game.setScreen(this.fightScreen);
    }

    public void setCrew1(Crew Crew1) {
        this.Crew1 = Crew1;
    }

    public void setCrew2(Crew Crew2) {
        this.Crew2 = Crew2;
    }

    public void setNewCrew1(Crew Crew1) {
        this.Crew1 = Crew1;
        System.out.println(Crew1.getName() + " Added to your Crew");
    }

    public void setNewCrew2(Crew Crew2) {
        this.Crew2 = Crew2;
        System.out.println(Crew2.getName() + " Added to your Crew");
    }

    public void setCrew1(String name, int maxLife, int life, int damage, Attack attack1, Attack attack2, Attack attack3) {
        this.Crew1 = new Crew(name, maxLife, life, damage, attack1, attack2, attack3);
    }

    public void setCrew2(String name, int maxLife, int life, int damage, Attack attack1, Attack attack2, Attack attack3) {
        this.Crew1 = new Crew(name, maxLife, life, damage, attack1, attack2, attack3);
    }

    public Crew getCrew1() {
        return Crew1;
    }

    public Crew getCrew2() {
        return Crew2;
    }

    public Enemy generateCombat () {
        Random random = new Random();
        int randomNumber = random.nextInt(3);

        switch (randomNumber) {
            case 0:
                return new Slug();
            case 1:
                return new Slime();
            case 2:
                return new Bebou();
            default:
                return new Slug();
        }
    }

    public void OpenSettings () {
        if (!SettingsWindows.IsOpen()) {
            pause();
            SettingsWindows.OpenSettings();
        } else {
            resume();
            SettingsWindows.CloseSettings();
        }
    }

    public void CheckOpenSettings ()
    {
        if (SettingsWindows == null) {
            SettingsWindows = new MainSettingsWindows();
        }
        if (SettingsWindows.IsOpen()) {
            SettingsWindows.DrawSettings(game, menu);
        }
    }

    public void changeScreen(Screen screenName)
    {
        switch(screenName)
        {
            case Travel: this.game.setScreen(this.travel); return;
            case Craft: this.game.setScreen(this.craftScreen); return;
            case Main : this.game.setScreen(this);
            default : this.game.setScreen(this);
        }
    }

    public void WarpToShip() {
        playerData.setPlanetLocation(0);
        ReloadMap();
    }

    public void ReloadMap() {
        show();
    }

    public void GenerateMap(String TypeOfGenerator) {
        CellularAutomaton generateMap = new CellularAutomaton(TypeOfGenerator);
    }

    private void setupGeneratedMap()
    {
        this.playerStart = getNavigableCoordInGeneratedMap();
        Vector2 ArtefactTarget = getNavigableCoordInGeneratedMap();

        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get("Walls");
        TiledMapTileLayer.Cell c = layer.getCell((int) ArtefactTarget.x, (int) ArtefactTarget.y);

        if (c != null)
        {
            if (c.getTile() != null)
            {
                TiledMapTileSet ts = this.map.getTileSets().getTileSet(0);

                System.out.println(ArtefactTarget);
                c.setTile(ts.getTile(558));

                System.out.println("ARTEFACT SET !");
            }
        }
    }
    private Vector2 getNavigableCoordInGeneratedMap()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get("Walls");
        TiledMapTileLayer.Cell c = layer.getCell(0, 0);
        Random r = new Random();
        int secureLoop = 0;

        while (secureLoop < 1000)
        {
            ++secureLoop;

            int x = r.nextInt(0, layer.getWidth());
            int y = r.nextInt(0, layer.getHeight());

            c = layer.getCell(x, y);

            if (c != null)
            {
                if (c.getTile().getProperties().containsKey("danger"))
                {
                    return new Vector2(x, y);
                }
            }

        }
        // System.out.println(secureLoop);
        return new Vector2(-100, -100);
    }

    public void WarpTo(int WarpToLocation) {
        if(warpConfirm == null) {
            warpConfirm = new WarpToConfirm(game);
        }
        if(!warpConfirm.IsToConfirm() && timeUntilToConfirm > 5f) {
            warpConfirm.ToConfirm(true);
            warpConfirm.startConfirmPopUp();
            warpConfirm.setLocation(WarpToLocation);
        }
    }
    public Vector2 getPlayerStart() { return this.playerStart; }
}