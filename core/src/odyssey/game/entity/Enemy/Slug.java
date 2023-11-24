package odyssey.game.entity.Enemy;

import com.badlogic.gdx.graphics.Texture;

public class Slug extends Enemy {

    public Slug() {
        setName("Slug");
        setSprite(new Texture("enemy/slug.png"));
        setMaxLife(50);
        setLife(50);
        setDamage(1);
        setSpeed(0.8f);
    }
}