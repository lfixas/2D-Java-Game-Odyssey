package odyssey.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import odyssey.game.entity.player.Inventory;

public class CraftUI extends Actor
{
    private Table table = null;
    private Stage stage = null;

    public CraftUI (Stage stage)
    {
        this.table = new Table();

        TextureRegionDrawable mainBack = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-dialog.png")));
        TextureRegionDrawable comboBack = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-button-off.png")));
        TextureRegionDrawable comboBackSelected = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/black-button-on.png")));
        BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"));
        TextureRegionDrawable vBar = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/verticalBar.bmp")));
        TextureRegionDrawable vKnob = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/craft/verticalKnob.bmp")));

        ScrollPane.ScrollPaneStyle paneStyle = new ScrollPane.ScrollPaneStyle(comboBack, vBar, vKnob, vBar, vKnob);
        List.ListStyle listStyle = new List.ListStyle(font, Color.WHITE, Color.RED, comboBackSelected);
        SelectBox.SelectBoxStyle style = new SelectBox.SelectBoxStyle(font, Color.WHITE, comboBack, paneStyle, listStyle);

        this.table.setBackground(mainBack);
        this.table.setSize(600.0f, 600.0f);
        SelectBox <String> selBox = new SelectBox<String>(style);
        selBox.setItems("Option1", "Option2", "Option3", "Option4", "Option5", "Option6", "Option7");
        selBox.setSelected("Option3");

        this.table.add(selBox);

        // this.setDebug(true, true);
        stage.addActor(this);
        this.stage = stage;
        this.setVisible(false);
    }
    @Override public void draw(Batch batch, float parentAlpha)
    {
        this.table.draw(batch, parentAlpha);
    }
    @Override
    public void setVisible(boolean bVisible)
    {
        if(bVisible)
        {
            this.table.setPosition(this.stage.getCamera().position.x - Math.round(this.table.getWidth() / 2),
                    this.stage.getCamera().position.y - Math.round(this.table.getHeight() / 2));

        }

        super.setVisible(bVisible);
    }

}
