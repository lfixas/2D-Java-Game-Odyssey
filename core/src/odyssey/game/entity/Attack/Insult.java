package odyssey.game.entity.Attack;

public class Insult extends Attack {

    public Insult() {
        setName("Insult him");
        setLore("You insult the %1$s..\nhe doesn't understand what you tell\nhim..\nhe's afraid..\nit take %2$s damage!");
        setDamage(0);
        setProperty("Stun");
    }
}
