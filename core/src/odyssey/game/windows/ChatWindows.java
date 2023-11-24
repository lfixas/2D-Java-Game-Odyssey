package odyssey.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.PlayerData.Crew;
import odyssey.game.entity.PlayerData.PlayerData;
import odyssey.game.screens.MainGameScreen;

import java.util.ArrayList;
import java.util.List;

public class ChatWindows {

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    private Texture background = new Texture(Gdx.files.internal("ui/chat/background.png"));

    private OdysseyGame game;

    private PlayerData playerData;

    private MainGameScreen main;

    private Crew AddAsCrewMember = null;

    private boolean chatting = false;

    public ChatWindows(OdysseyGame game, PlayerData playerData, MainGameScreen main) {
        this.game = game;
        this.playerData = playerData;
        this.main = main;
    }

    public void Chat(boolean bool) {
        chatting = bool;
    }

    public boolean IsChatting() {
        return chatting;
    }

    public void ChatPopUp(String chat, Crew AddAsCrewMember) {
        this.AddAsCrewMember = AddAsCrewMember;
        float Box_Chat_X = OdysseyGame.WIDTH * 0.05f;
        float Box_Chat_Y = OdysseyGame.HEIGHT * 0.05f;

        float Box_Chat_WIDTH = OdysseyGame.WIDTH - Box_Chat_X * 2;
        float Box_Chat_HEIGHT = OdysseyGame.HEIGHT * 0.3f;

        float Text_Chat_WIDTH = (Box_Chat_WIDTH - Box_Chat_X);
        float Text_Chat_HEIGHT = (Box_Chat_HEIGHT - Box_Chat_Y);

        if(chatting) {
            ConversationSplitter(chat);
            ChatOptions();
            ChatMessage();

            game.batch.begin();
            game.batch.draw(background, Box_Chat_X, Box_Chat_Y, Box_Chat_WIDTH, Box_Chat_HEIGHT);
            font.setColor(Color.WHITE);
            font.draw(game.batch, currentText, Box_Chat_X * 1.4f, Box_Chat_Y * 1.4f + Text_Chat_HEIGHT, Text_Chat_WIDTH, -1, true);
            game.batch.end();
        }
    }


    private float timeSeconds;
    private float period;
    private String fullText = "";
    private String currentText = "";
    private int index;
    private int state;

    public void startChat() {
        fullText = "";
        timeSeconds = 0f;
        period = 0.1f;
        index = 0;
        state = 0;
    }

    private void ChatMessage() {
        timeSeconds += Gdx.graphics.getDeltaTime();
        if(timeSeconds > period) {
            timeSeconds -= period;
            if(fullText.contains("   %AddAsCrewMember%   ")) {
                main.setNewCrew1(AddAsCrewMember);
                fullText = "";
                index = 0;
                Chat(false);
            }
            if(fullText.contains("* ")) {
                index = fullText.length() - 1;
                ChatChoice();
            }
            if (index < fullText.length()) {
                currentText = fullText.substring(0, index + 1);
                index++;
            } else if (index == fullText.length()) {
                index++;
                state++;
            }
        }
    }

    private void ChatOptions() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && index < fullText.length()) {
            index = fullText.length() - 1;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && index > fullText.length()) {
            currentText = "";
            index = 0;
            if(state < fullConversation.size()) {
                fullText = fullConversation.get(state);
            } else {
                Chat(false);
            }
        }
    }

    private List<String> fullConversation;

    private void ConversationSplitter(String chat) {
        String[] conversation = chat.split("<");
        List<String> conversationList = new ArrayList<>();

        for(String part : conversation) {
            conversationList.add(part);
        }

        fullConversation = conversationList;
    }

    private void ChatChoice() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            state += 1;
            index = 0;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && state + 4 < fullConversation.size()) {
            state += 4;
            index = 0;
        }
        if(state < fullConversation.size()) {
            fullText = fullConversation.get(state);
        } else {
            Chat(false);
        }
    }
}
