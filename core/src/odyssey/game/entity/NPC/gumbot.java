package odyssey.game.entity.NPC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import odyssey.game.entity.Attack.Overheat;
import odyssey.game.entity.Attack.Rush;
import odyssey.game.entity.Attack.ThrowRock;
import odyssey.game.entity.PlayerData.Crew;

public class gumbot extends npc {

    public gumbot() {
        setSprite(new Texture(Gdx.files.internal("entity/gumbot/down_0.png")));
        setChat("<Greetings, Master I am GumBot, your loyal assistant on this interstellar adventure.\nHow may I be of service to you today?<If you want I will do my best to assist you on the ground.<1. * Follow me\n\n\n\n4. * Deny<Understood, Master I am ready to assist you in combat. Please provide instructions during combat on how you'd like me to engage in the upcoming battle.<   %AddAsCrewMember%   < <OK, Master. If you ever require my assistance, feel free to call upon GumBot. I'll be here to support you on your interstellar journey. Safe travels!");
    }

    public Crew AddAsCrewMember() {
        return new Crew("GumBot", 50, 50, 1.2f, new ThrowRock(), new Rush(), new Overheat());
    }
}
