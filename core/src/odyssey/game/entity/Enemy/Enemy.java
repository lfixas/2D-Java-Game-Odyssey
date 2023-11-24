package odyssey.game.entity.Enemy;

import com.badlogic.gdx.graphics.Texture;

public abstract class Enemy {
    private String name;
    private Texture sprite;
    private int maxLife;
    private int life;
    private int damage;
    private float speed;

    public void setName(String name) {
        this.name = name;
    }

    public void setSprite(Texture sprite) {this.sprite = sprite; }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public void setLife(int life) { this.life = life; }

    public void setDamage(int damage) { this.damage = damage; }

    public void setSpeed(float speed) { this.speed = speed; }

    public String getName() {
        return name;
    }

    public Texture getSprite() { return sprite; }

    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() { return speed; }
}
