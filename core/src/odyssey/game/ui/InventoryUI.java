package odyssey.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import odyssey.game.entity.player.Inventory;
import com.badlogic.gdx.graphics.Texture;

public class InventoryUI extends Actor
{
    private Stage stage = null;
    private Inventory inventory = null;
    private Table table = null;
    private Label.LabelStyle style = null;
    public InventoryUI(Stage stage, Inventory inventory)
    {
        this.stage = stage;
        this.inventory = inventory;
        this.table = new Table();

        TextureRegionDrawable text = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/travel/screen.png")));

        this.table.setBackground(text);
        this.table.setSize(600.0f, 600.0f);
        this.table.setDebug(true, true);
        // this.table.setDebug(true, true);

        BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"));
        this.style = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label("Inventaire :", style);

        this.table.defaults().top();
        this.table.add(label).padLeft(35.0f).padBottom(50.0f);
        this.table.defaults().left();
        this.table.defaults().space(25.0f, 50.0f, 25.0f, 50.0f);

        refreshItemsList();

        this.stage.addActor(this);
        this.table.defaults().reset();
        super.setVisible(false);
    }
    @Override
    public void act(float delta)
    {
        this.table.setPosition(this.stage.getCamera().position.x - Math.round(this.table.getWidth() / 2),
                this.stage.getCamera().position.y - Math.round(this.table.getHeight() / 2));
    }
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        this.table.draw(batch, parentAlpha);
    }
    @Override
    public void setVisible(boolean bVisible)
    {
        if(bVisible)
        {
            Label temp = (Label) this.table.getChildren().get(0);

            this.table.clear();
            this.table.add(temp).padLeft(35.0f).padBottom(50.0f);

            refreshItemsList();
        }

        super.setVisible(bVisible);
    }
    private void refreshItemsList()
    {
        this.table.defaults().left();
        this.table.defaults().space(25.0f, 50.0f, 25.0f, 50.0f);

        for (Inventory.RessourceItem item : Inventory.RessourceItem.values())
        {
            Label name = new Label(item.name(), this.style);
            Label count = new Label(String.valueOf(this.inventory.getRessourceCount(item.ordinal()) + "x"), this.style);

            this.table.row().pad(0.0f, 50.0f, 10.0f, 50.0f);
            this.table.add(name);
            this.table.add(count);
        }

        this.table.defaults().reset();
    }
}
