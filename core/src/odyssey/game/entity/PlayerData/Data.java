package odyssey.game.entity.PlayerData;

import odyssey.game.entity.Attack.Attack;

public abstract class Data {
    private String name;
    private int maxLife;
    private int life;
    private float damage;

    private Attack attack1, attack2, attack3;

    public void setName(String name) { this.name = name; }

    public void setMaxLife(int maxLife) { this.maxLife = maxLife; }

    public void setLife(int life) { this.life = life; }

    public void setDamage(float damage) { this.damage = damage; }

    public void setAttack1(Attack attack) { this.attack1 = attack; }
    public void setAttack2(Attack attack) { this.attack2 = attack; }
    public void setAttack3(Attack attack) { this.attack3 = attack; }

    public String getName () { return name;}

    public int getMaxLife() { return maxLife; }

    public int getLife() { return life; }

    public float getDamage() { return damage; }

    public Attack getAttack1() { return attack1; }
    public Attack getAttack2() { return attack2; }
    public Attack getAttack3() { return attack3; }

}
