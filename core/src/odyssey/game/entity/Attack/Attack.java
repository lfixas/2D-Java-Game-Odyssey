package odyssey.game.entity.Attack;

public abstract class Attack{

    private String name;

    private String lore;

    private int damage;

    private String property = "none";

    public void setName(String name) { this.name = name; }

    public void setLore(String lore) { this.lore = lore; }

    public void setDamage(int damage) { this.damage = damage; }

    public void setProperty(String property) { this.property = property;}

    public String getName() { return name; }

    public String getLore() { return lore; }

    public int getDamage() { return damage; }

    public String getProperty() { return property; }
}
