package odyssey.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.craftdata.CraftData;
import odyssey.game.entity.player.Inventory;
import odyssey.game.entity.player.Player;
import odyssey.game.entity.player.PlayerController;
import odyssey.game.entity.ship.Starship;
import odyssey.game.ui.ProgressBarUI;

import java.util.Arrays;

public class MainCraftScreen implements Screen
{
    private Table mainTable = new Table();
    private Table schTable = new Table();
    private Table invTable = new Table();
    private Stage stage = new Stage();
    private ImageTextButton buttonClicked = null;
    private Array <ImageTextButton> buttons = null;
    private MainGameScreen mainScreen = null;
    private Image blankImg = null;
    private Image grenadeImg = null;
    private Image potionImg = null;
    private BitmapFont font = null;
    private float maxPercentPB = 0.0f;
    private float currentPercentPB = 0.0f;
    public MainCraftScreen(final MainGameScreen mainScreen)
    {
        this.mainScreen = mainScreen;

        TextureRegionDrawable mainBack = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-dialog.png")));
        TextureRegionDrawable textOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-button-off.png")));
        TextureRegionDrawable textOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-button-on.png")));

        this.font = new BitmapFont(Gdx.files.internal("font/main.fnt"));

        ImageTextButton.ImageTextButtonStyle styleBasic = new ImageTextButton.ImageTextButtonStyle(textOff, textOff, textOn, font);
        ImageTextButton.ImageTextButtonStyle styleGreen = new ImageTextButton.ImageTextButtonStyle(textOff, textOff, textOn, font);
        ImageTextButton.ImageTextButtonStyle styleRed = new ImageTextButton.ImageTextButtonStyle(textOff, textOff, textOn, font);

        ImageTextButton btnUpgrade = new ImageTextButton("UPGRADE", styleBasic);
        ImageTextButton btnPot = new ImageTextButton("HEALTH", styleBasic);
        ImageTextButton btnWeap = new ImageTextButton("BAZOOKA", styleBasic);
        ImageTextButton btnCraft = new ImageTextButton("CRAFT", styleGreen);
        ImageTextButton btnLeave = new ImageTextButton("CLOSE", styleRed);

        this.blankImg = new Image();
        this.potionImg = new Image(new Texture(Gdx.files.internal("ui/craft/aid.jpg")));
        this.grenadeImg = new Image(new Texture(Gdx.files.internal("ui/craft/RPG.jpg")));

        this.buttons = new Array<ImageTextButton>(3);
        this.buttons.add(btnUpgrade);
        this.buttons.add(btnPot);
        this.buttons.add(btnWeap);
        this.buttons.get(0).setChecked(true);
        this.buttonClicked = this.buttons.get(0);

        // DEBUG :
        // this.mainTable.setDebug(true, true);

        this.mainTable.setSize(OdysseyGame.WIDTH, OdysseyGame.HEIGHT);
        this.mainTable.setBackground(mainBack);
        this.mainTable.add(btnUpgrade).width(400.0f).colspan(2).center();
        this.mainTable.add(btnPot).width(400.0f).colspan(2).center();
        this.mainTable.add(btnWeap).width(400.0f).colspan(2).center();

        this.schTable.setBackground(mainBack);
        this.invTable.setBackground(mainBack);

        this.mainTable.row();
        this.mainTable.add(this.schTable).width(505.0f).colspan(3);

        this.mainTable.add(this.invTable).width(505.0f).colspan(3);
        this.mainTable.row();

        this.mainTable.add(btnCraft).colspan(3).padTop(10.0f);
        this.mainTable.add(btnLeave).colspan(3).padTop(10.0f);

        for (ImageTextButton btn : this.buttons)
        {
            btn.addListener(new ClickListener()
            {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    ImageTextButton b = (ImageTextButton) event.getListenerActor();
                    // System.out.println("Cliked on :" + b.getText());
                    setButtonClicked(b);
                    updateSchTable();

                    return true;
                }
            });
        }
        btnLeave.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                leave();
                return true;
            }
        });
        btnCraft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                craft();
                updateInvTable();
                updateSchTable();

                return true;
            }

        });

        this.stage.addActor(this.mainTable);
    }
    public void show()
    {
        Gdx.input.setInputProcessor(stage);

        updateInvTable();
        updateSchTable();
    }
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        for (ImageTextButton btn : this.buttons)
        {
            if (btn != this.buttonClicked)
                btn.setChecked(false);
            else
                btn.setChecked(true);
        }

        this.stage.draw();

        interpProgressBar(delta / 2);

        ShapeRenderer shp = new ShapeRenderer();

        shp.begin(ShapeRenderer.ShapeType.Filled);
        shp.setColor(Color.BLUE);
        shp.rect(113.0f, 410.0f, 450.0f * this.currentPercentPB, 80.0f);
        shp.end();
    }
    public void resize ( int width, int height)
    {

    }
    public void pause ()
    {
    }
    public void resume ()
    {

    }
    public void hide ()
    {

    }
    public void dispose ()
    {
    }
    private void setButtonClicked(ImageTextButton btn)
    {
        this.buttonClicked = btn;
    }
    private void leave() { this.mainScreen.changeScreen(MainGameScreen.Screen.Main); }
    private void updateInvTable()
    {
        Inventory inv = PlayerController.getPlayer().getInventory();
        this.invTable.reset();
        this.invTable.add(new Label("INVENTORY :", new Label.LabelStyle(this.font, Color.WHITE))).colspan(3).padTop(10.0f).padBottom(10.0f);
        this.invTable.row();

        for (Inventory.RessourceItem item : Inventory.RessourceItem.values())
        {
            this.invTable.add(new Label(item.name() + ":", new Label.LabelStyle(this.font, Color.WHITE)));
            this.invTable.add(new Label( inv.getRessourceCount(item.ordinal()) + "x", new Label.LabelStyle(this.font, Color.WHITE)));
            this.invTable.row();
        }
    }
    private void updateSchTable()
    {
        this.schTable.reset();

        if (this.buttonClicked == this.buttons.get(0))
        {
            PlayerController.getPlayer().getInventory().loadShipState();
            this.maxPercentPB = Starship.getState() / 100.0f;
            Label lblProgress = new Label("SHIP STATE :", new Label.LabelStyle(font, Color.WHITE));

            this.schTable.add(lblProgress).colspan(3).padTop(10.0f);
            this.schTable.row();
            this.schTable.add(this.blankImg).height(150.0f).padTop(10.0f).padBottom(10.0f);
        }
        else
        {
            this.maxPercentPB = 0.0f;

            if (this.buttonClicked == this.buttons.get(1))
            {
                this.schTable.add(new Label("HEALTH :", new Label.LabelStyle(font, Color.WHITE))).colspan(3).padTop(10.0f);
                this.schTable.row();
                this.schTable.add(this.potionImg).height(150.0f).width(170.0f).colspan(3).padTop(10.0f).padBottom(10.0f);
                this.schTable.add(new Label(PlayerController.getPlayer().getInventory().getCraftCount(CraftData.CraftItem.Potion) + "x",
                        new Label.LabelStyle(font, Color.WHITE)));
            }

            if (this.buttonClicked == this.buttons.get(2))
            {
                this.schTable.add(new Label("BAZZOKA :", new Label.LabelStyle(font, Color.WHITE))).colspan(3).padTop(10.0f);
                this.schTable.row();
                this.schTable.add(this.grenadeImg).height(150.0f).width(170.0f).padTop(10.0f).padBottom(10.0f);
                this.schTable.add(new Label(PlayerController.getPlayer().getInventory().getCraftCount(CraftData.CraftItem.Weapon) + "x",
                        new Label.LabelStyle(font, Color.WHITE))).colspan(3);
            }
        }

        this.schTable.row();
        this.schTable.add(new Label("COMPONENTS :", new Label.LabelStyle(font, Color.WHITE))).colspan(3).padBottom(10.0f);

        for (Inventory.RessourceItem ressource : Inventory.RessourceItem.values())
        {
            this.schTable.row();

            this.schTable.add(new Label(ressource.name() + ":", new Label.LabelStyle(font, Color.WHITE)));

            CraftData.CraftItem scheme = getCraftSelected();
            Color color = checkEnoughRessource(scheme, ressource) ? Color.WHITE : Color.RED;

            this.schTable.add(new Label(CraftData.getSchemes(scheme).get(ressource)
                    + "x", new Label.LabelStyle(font, color))).colspan(3);

        }
    }
    private void craft()
    {
        CraftData.CraftItem scheme = getCraftSelected();
        IntIntMap components = new IntIntMap(5);

        for (Inventory.RessourceItem ressource : Inventory.RessourceItem.values())
        {
            // Possible de display un message
            if (!checkEnoughRessource(scheme, ressource))
                return;

            components.put(ressource.ordinal(), CraftData.getSchemes(scheme).get(ressource));
        }

        for (Inventory.RessourceItem ressource : Inventory.RessourceItem.values())
            PlayerController.getPlayer().getInventory().addRessourceItem(ressource.ordinal(), - components.get(ressource.ordinal(), 0));

        if (scheme == CraftData.CraftItem.Upgrade)
        {
            Starship.repair();
            PlayerController.getPlayer().getInventory().saveShipState();
        }
        else
            PlayerController.getPlayer().getInventory().addCraftItem(scheme, 1);
    }
    private CraftData.CraftItem getCraftSelected()
    {
        if (this.buttonClicked == this.buttons.get(0))
            return CraftData.CraftItem.Upgrade;

        else if (this.buttonClicked == this.buttons.get(1))
            return CraftData.CraftItem.Potion;

        else
            return CraftData.CraftItem.Weapon;
    }
    private boolean checkEnoughRessource(CraftData.CraftItem scheme, Inventory.RessourceItem item)
    {
        int stock = PlayerController.getPlayer().getInventory().getRessourceCount(item.ordinal());
        int required = CraftData.getSchemes(scheme).get(item);

        return stock >= required;
    }
    private void interpProgressBar(float deltaTime)
    {
        this.currentPercentPB = MathUtils.clamp(this.currentPercentPB +  deltaTime, 0.0f, this.maxPercentPB);
    }

}
