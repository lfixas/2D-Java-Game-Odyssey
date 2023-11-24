package odyssey.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressBarUI extends Actor
{
    private float percent = 0.0f;

    private Vector2 position = new Vector2(0, 0);

    public void setPercent(float percent)
    {
        if (Float.compare(percent, 1.0f) > 0)
            this.percent = 1.0f;
        else if (Float.compare(percent, 0.0f) < 0)
            this.percent = 0.0f;
        else
            this.percent = percent;
    }
    public void addPercent(float add)
    {
        if (Float.compare(this.percent + add, 1.0f) > 0)
            this.percent = 1.0f;

        else if (Float.compare(this.percent + add, 0.0f) < 0)
            this.percent = 0.0f;

        else
            this.percent += add;
    }

    public void setPosition(int x, int y)
    {
        this.position.x = x;
        this.position.y = y;
    }
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        System.out.println("draw");

        Pixmap pixmap = new Pixmap( 64, 64, Pixmap.Format.RGBA8888 );
        pixmap.fill();
        pixmap.setColor( 0, 1, 0, 0.75f );
        pixmap.fillRectangle(0, 0, 450, 60);
    }
}
