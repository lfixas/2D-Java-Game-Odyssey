package odyssey.game.entity.Enemy;

import com.badlogic.gdx.graphics.Texture;

public class Slime extends Enemy {

    public Slime() {
        setName("Slime");
        setSprite(new Texture("enemy/slime.png"));
        setMaxLife(20);
        setLife(20);
        setDamage(3);
        setSpeed(0.4f);
    }
}