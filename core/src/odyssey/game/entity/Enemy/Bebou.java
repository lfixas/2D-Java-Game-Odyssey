package odyssey.game.entity.Enemy;

import com.badlogic.gdx.graphics.Texture;

public class Bebou extends Enemy {

    public Bebou() {
        setName("Bebou");
        setSprite(new Texture("enemy/bebou.png"));
        setMaxLife(100);
        setLife(100);
        setDamage(8);
        setSpeed(5.0f);
    }
}