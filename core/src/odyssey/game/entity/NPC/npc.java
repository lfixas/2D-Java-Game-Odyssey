package odyssey.game.entity.NPC;

import com.badlogic.gdx.graphics.Texture;
import odyssey.game.entity.PlayerData.Crew;

public abstract class npc {
    private Texture sprite;
    private String chat;

    public void setSprite(Texture sprite) { this.sprite = sprite; }

    public void setChat(String chat) { this.chat = chat; }

    public Texture getSprite() { return sprite; }

    public String getChat() { return chat; }

    public Crew AddAsCrewMember() {return null; }
}
