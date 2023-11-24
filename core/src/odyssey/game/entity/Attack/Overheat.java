package odyssey.game.entity.Attack;

public class Overheat extends Attack {

    public Overheat() {
        setName("Overheat");
        setLore("You overheat yourself\nwhich increases your team damage\nby 1.");
        setDamage(0);
        setProperty("DamageBuff");
    }
}
