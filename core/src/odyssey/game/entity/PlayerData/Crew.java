package odyssey.game.entity.PlayerData;

import odyssey.game.entity.Attack.Attack;

public class Crew extends Data {

    public Crew(String name, int maxLife, int life, float damage, Attack attack1, Attack attack2, Attack attack3) {
        setName(name);
        setMaxLife(maxLife);
        setLife(life);
        setDamage(damage);
        setAttack1(attack1);
        setAttack2(attack2);
        setAttack3(attack3);
    }
}
