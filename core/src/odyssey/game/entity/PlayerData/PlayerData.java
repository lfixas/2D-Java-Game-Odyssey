package odyssey.game.entity.PlayerData;

import odyssey.game.entity.Attack.Attack;
import odyssey.game.entity.Attack.Blaster2000;
import odyssey.game.entity.Attack.Insult;
import odyssey.game.entity.Attack.ThrowRock;
import odyssey.game.entity.player.PlayerController;
import odyssey.game.entity.ship.Starship;

public class PlayerData extends Data {

    private float speed;

    private int unlocked;

    private int planetLocation;

    public PlayerData() {
        setName(System.getProperty("user.name").substring(0, 1).toUpperCase() + System.getProperty("user.name").substring(1));
        setMaxLife(20);
        setLife(20);
        setDamage(1.0f);
        setAttack1(new ThrowRock());
        setAttack2(new Blaster2000());
        setAttack3(new Insult());
        setSpeed(2.0f);

        setUnlocked(3);
        setPlanetLocation(0);
    }
    public void updateUnlocked()
    {
        PlayerController.getPlayer().getInventory().loadShipState();

        if (Starship.getState() <= 25)
            setUnlocked(3);

        else if (Starship.getState() > 25 & Starship.getState()  <= 50)
            setUnlocked(4);

        else if (Starship.getState() > 50 & Starship.getState()  <= 75)
            setUnlocked(5);

        else
            setUnlocked(7);
    }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getSpeed() { return speed; }

    public void setUnlocked(int unlocked) { this.unlocked = unlocked; }

    public void setPlanetLocation(int planetLocation) { this.planetLocation = planetLocation; }

    public int getUnlocked() { return unlocked; }

    public int getPlanetLocation() { return planetLocation; }
}
