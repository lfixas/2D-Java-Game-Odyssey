package odyssey.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import odyssey.game.OdysseyGame;
import odyssey.game.screens.MainGameScreen;

import java.awt.image.TileObserver;
import java.util.Arrays;
import java.util.List;

public class Player extends Actor {
    OdysseyGame game;
    public int speed = 1000;
    private Sprite charSprite = null;
    private Animation <TextureRegion> moveUp = null;
    private Animation <TextureRegion> moveDown = null;
    private Animation <TextureRegion> moveLeft = null;
    private Animation <TextureRegion> moveRight = null;
    private Animation <TextureRegion> playingAnimation = null;
    private float ellapsedFrameTime = 0.0f;
    private OrthographicCamera camera = null;
    private MainGameScreen mainScreen = null;
    private Inventory inventory = new Inventory();

    private float FightChance = 0.05f;
    private float previousFight = 0f;

    private final float frameDuration = 0.20f;

    public Player(OdysseyGame game, OrthographicCamera camera, MainGameScreen mainScreen)
    {
        this.charSprite = new Sprite(new Texture(Gdx.files.internal("player/down_1.png")));
        setBounds(this.charSprite.getX(), this.charSprite.getY(), this.charSprite.getWidth(), this.charSprite.getHeight());
        setName("Player");

        addAction(new moveAction(this));

        this.game = game;
        this.setSceneCamera(camera);
        this.mainScreen = mainScreen;
        this.inventory = new Inventory();

        Gdx.input.setInputProcessor(new PlayerController(this));

        TextureAtlas atlas = null;

        try
        {
            atlas = new TextureAtlas(Gdx.files.internal("TexturesPack/player.atlas"));

            this.moveUp = new Animation<TextureRegion> (frameDuration, atlas.findRegions("up"), PlayMode.LOOP);
            this.moveDown = new Animation<TextureRegion> (frameDuration, atlas.findRegions("down"), PlayMode.LOOP);
            this.moveLeft = new Animation<TextureRegion> (frameDuration, atlas.findRegions("left"), PlayMode.LOOP);
            this.moveRight = new Animation<TextureRegion> (frameDuration, atlas.findRegions("right"), PlayMode.LOOP);

            this.playingAnimation = this.moveDown;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        TiledMapTileLayer map = (TiledMapTileLayer) this.mainScreen.getMapLayer("BaseLayer");

        System.out.println(this.mainScreen.getPlayerStart());

        if (this.mainScreen.getPlayerStart().epsilonEquals(new Vector2(-100, -100)))
        {
            System.out.println("Default placement ! ");

            this.setPosition((map.getWidth() * map.getTileWidth() * this.mainScreen.getMapScale()) / 4,
                    (map.getHeight() * map.getTileWidth()) * this.mainScreen.getMapScale() / 4);
        }
        else
            this.setPosition((this.mainScreen.getPlayerStart().x * map.getTileWidth() * this.mainScreen.getMapScale()),
                    (this.mainScreen.getPlayerStart().y * map.getTileWidth()) * this.mainScreen.getMapScale());

        updateCamera();
    }
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(this.playingAnimation.getKeyFrame(this.ellapsedFrameTime), this.getX(), this.getY());
    }
    public Rectangle getSpriteBounds() { return this.charSprite.getBoundingRectangle(); }
    public Inventory getInventory() { return this.inventory; }
    public void updateMotion(String direction, float deltaTime)
    {
        if (this.ellapsedFrameTime >= this.playingAnimation.getAnimationDuration())
            this.ellapsedFrameTime = 0.0f;
        else
            this.ellapsedFrameTime += deltaTime;

        Vector2 vec = new Vector2(0.0f, 0.0f);

        switch(direction)
        {
            case "UP" :
                vec.add(0.0f, this.speed * deltaTime);
                this.playingAnimation = this.moveUp;
                break;
            case "DOWN" :
                vec.add(0.0f, -(this.speed * deltaTime));
                this.playingAnimation = this.moveDown;
                break;
            case "LEFT" :
                vec.add(-(this.speed * deltaTime), 0.0f);
                this.playingAnimation = this.moveLeft;
                break;
            case "RIGHT" :
                vec.add(this.speed * deltaTime, 0.0f);
                this.playingAnimation = this.moveRight;
                break;
            default : return;
        }
        if (!this.checkCollision(vec))
            this.moveBy(vec.x, vec.y);

        if(this.checkDanger(vec))
            this.DangerFight();

        if(this.checkWarpTo(vec) != 0)
            mainScreen.WarpTo(this.checkWarpTo(vec));

        // update camera based on player position :
        this.updateCamera();
    }

    public void setSceneCamera(OrthographicCamera camera)
    {
        this.camera = camera;
        this.updateCamera();
    }
    public void updateCamera()
    {
        this.camera.position.set(this.getX() + this.getSpriteBounds().width / 2,
                this.getY() + this.getSpriteBounds().height / 2, 0.0f);
    }
    public void displayInventory()
    {
        this.inventory.printInventoryDebug();

        boolean bVisible = this.mainScreen.getInventoryUI().isVisible();

        this.mainScreen.getInventoryUI().setVisible(!bVisible);
    }
    public void clicHandle(Vector2 screenPos)
    {
        List <String> layNames = Arrays.asList("Ressources", "Machines");
        Vector2 proj = this.mainScreen.getViewport().unproject(screenPos);
        TiledMapTileLayer layer = null;

        for (String name : layNames)
        {
            layer = (TiledMapTileLayer) this.mainScreen.getMapLayer(name);

            if (layer != null)
                break;
        }

        if (layer == null)
            return;

        int tileSize = layer.getTileHeight();
        float mapScale = this.mainScreen.getMapScale();

        int X = (int) (Math.floor(proj.x / (tileSize * mapScale)));
        int Y = (int) (Math.floor(proj.y / (tileSize * mapScale)));

        Cell c = layer.getCell(X, Y);

        if (c == null)
            return;

        if (c.getTile() == null)
            return;

        if (checkDistance(proj.x, proj.y))
        {
            System.out.println("Interaction found !");

            if (c.getTile().getProperties().containsKey("type"))
            {
                this.inventory.addRessourceItem((int) c.getTile().getProperties().get("type"), 1);
                c.setTile(null);

                return;
            }

            if (c.getTile().getProperties().containsKey("fonction"))
            {
                System.out.println(c.getTile().getProperties().get("fonction"));
                objectInteraction((String) c.getTile().getProperties().get("fonction"));

                return;
            }
        }
    }
    private boolean checkCollision(Vector2 moveBy)
    {
        TiledMapTileLayer base = (TiledMapTileLayer) this.mainScreen.getMapLayer("BaseLayer");
        TiledMapTileLayer ressources = (TiledMapTileLayer) this.mainScreen.getMapLayer("Ressources");
        TiledMapTileLayer walls = (TiledMapTileLayer) this.mainScreen.getMapLayer("Walls");

        Vector2 proj = getCenteredPlayerPos();

        int tileSize = base.getTileHeight();
        float mapScale = this.mainScreen.getMapScale();

        int X = (int) Math.floor((proj.x + moveBy.x) / (tileSize * mapScale));

        // SpriteBounds.height / 4 => trop d'espace au dessus du sprite, prendre un peu en dessous.
        int Y = (int) Math.floor(((proj.y + moveBy.y) - this.getSpriteBounds().height / 4) / (tileSize * mapScale));

        Cell cBase = null;
        Cell cRes = null;
        Cell cWalls = null;

        if (base != null)
            cBase = base.getCell(X, Y);
        else
            return true;

        if (ressources != null)
            cRes = ressources.getCell(X, Y);

        if (walls != null)
            cWalls = walls.getCell(X, Y);

        boolean baseCol = false;
        boolean resCol = false;
        boolean wallsCol = false;

        if (getCellProperty(cBase, "collision") != null)
            baseCol = (boolean) getCellProperty(cBase, "collision");

        if (getCellProperty(cRes, "collision") != null)
            resCol = (boolean) getCellProperty(cRes, "collision");

        if (getCellProperty(cWalls, "collision") != null)
            wallsCol = (boolean) getCellProperty(cWalls, "collision");

        return baseCol | resCol | wallsCol;
    }
    private boolean checkDanger(Vector2 moveBy)
    {
        TiledMapTileLayer base = (TiledMapTileLayer) this.mainScreen.getMapLayer("BaseLayer");

        Vector2 proj = getCenteredPlayerPos();

        int tileSize = base.getTileHeight();
        float mapScale = this.mainScreen.getMapScale();

        int X = (int) Math.floor((proj.x + moveBy.x) / (tileSize * mapScale));

        // SpriteBounds.height / 4 => trop d'espace au dessus du sprite, prendre un peu en dessous.
        int Y = (int) Math.floor(((proj.y + moveBy.y) - this.getSpriteBounds().height / 4) / (tileSize * mapScale));

        Cell cBase = null;

        if (base != null)
            cBase = base.getCell(X, Y);

        boolean baseCol = false;

        if (getCellProperty(cBase, "danger") != null)
            baseCol = (boolean) getCellProperty(cBase, "danger");

        return baseCol;
    }

    private int checkWarpTo(Vector2 moveBy)
    {
        TiledMapTileLayer base = (TiledMapTileLayer) this.mainScreen.getMapLayer("BaseLayer");
        TiledMapTileLayer ressources = (TiledMapTileLayer) this.mainScreen.getMapLayer("Ressources");

        Vector2 proj = getCenteredPlayerPos();

        int tileSize = base.getTileHeight();
        float mapScale = this.mainScreen.getMapScale();

        int X = (int) Math.floor((proj.x + moveBy.x) / (tileSize * mapScale));

        // SpriteBounds.height / 4 => trop d'espace au dessus du sprite, prendre un peu en dessous.
        int Y = (int) Math.floor(((proj.y + moveBy.y) - this.getSpriteBounds().height / 4) / (tileSize * mapScale));

        Cell cRes = null;

        if (ressources != null)
            cRes = ressources.getCell(X, Y);

        int warpLocation = 0;

        if (getCellProperty(cRes, "WarpTo") != null)
            warpLocation = (int) getCellProperty(cRes, "WarpToLocation");

        return warpLocation;
    }
    private Object getCellProperty(Cell cell, String property)
    {
        if (cell == null)
            return null;

        if (cell.getTile() == null)
            return null;

        if (cell.getTile().getProperties().containsKey(property))
            return cell.getTile().getProperties().get(property);
        else
            return null;
    }

    private Vector2 getCenteredPlayerPos()
    {
        Vector2 vec = new Vector2(OdysseyGame.WIDTH / 2,  OdysseyGame.HEIGHT / 2);

        return this.mainScreen.getViewport().unproject(vec);
    }

    private boolean checkDistance(float posx, float posy)
    {
        Vector2 player = getCenteredPlayerPos();

        // System.out.println("Distance to point : " + Vector2.dst(posx, posy, player.x, player.y));

        if (Float.compare(Vector2.dst(posx, posy, player.x, player.y), 150.0f) < 0)
            return true;
        else
            return false;

    }
    private void objectInteraction(String objectName)
    {
        if (objectName.equals("map"))
            this.mainScreen.changeScreen(MainGameScreen.Screen.Travel);

        if (objectName.equals("craft"))
        {
            this.mainScreen.changeScreen(MainGameScreen.Screen.Craft);
        }

    }

    private void DangerFight() {
        boolean StartFight = Math.random() < FightChance;
        previousFight += Gdx.graphics.getDeltaTime();
        if(StartFight && previousFight > 15f) {
            previousFight = 0f;
            mainScreen.fightStart();
        }
    }
}
