package odyssey.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sun.org.apache.xpath.internal.operations.Bool;
import odyssey.game.OdysseyGame;
import odyssey.game.entity.PlayerData.Crew;
import odyssey.game.entity.PlayerData.PlayerData;
import odyssey.game.screens.MainGameScreen;

public class WarpToConfirm {

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/main.fnt"),Gdx.files.internal("font/main.png"),false);

    private Texture background = new Texture(Gdx.files.internal("ui/chat/background.png"));

    private OdysseyGame game;

    private boolean toConfirm = false;

    private String isConfirmed = "";

    private int WarpToLocation;

    public WarpToConfirm(OdysseyGame game) {
        this.game = game;
    }

    public void ToConfirm(boolean bool) { toConfirm = bool; }

    public boolean IsToConfirm() { return toConfirm; }

    public String IsConfirmed() { return isConfirmed; }

    public void setLocation(int WarpToLocation) { this.WarpToLocation = WarpToLocation; }

    public int getLocation() { return WarpToLocation; }

    public void ConfirmPopUp() {
        float Box_Chat_X = OdysseyGame.WIDTH * 0.05f;
        float Box_Chat_Y = OdysseyGame.HEIGHT * 0.05f;

        float Box_Chat_WIDTH = OdysseyGame.WIDTH - Box_Chat_X * 2;
        float Box_Chat_HEIGHT = OdysseyGame.HEIGHT * 0.3f;

        float Text_Chat_WIDTH = (Box_Chat_WIDTH - Box_Chat_X);
        float Text_Chat_HEIGHT = (Box_Chat_HEIGHT - Box_Chat_Y);

        if(toConfirm) {
            ConfirmOptions();
            ConfirmPopUpMessage();

            game.batch.begin();
            game.batch.draw(background, Box_Chat_X, Box_Chat_Y, Box_Chat_WIDTH, Box_Chat_HEIGHT);
            font.setColor(Color.WHITE);
            font.draw(game.batch, currentText, Box_Chat_X * 1.4f, Box_Chat_Y * 1.4f + Text_Chat_HEIGHT, Text_Chat_WIDTH, -1, true);
            game.batch.end();

            if(index > fullText.length()) {
                game.batch.begin();
                font.setColor(Color.CYAN);
                font.draw(game.batch, "\n\n\n* Y(es)", Box_Chat_X * 1.4f + Box_Chat_WIDTH * 0.2f, Box_Chat_Y * 1.4f + Text_Chat_HEIGHT, Text_Chat_WIDTH, -1, true);
                game.batch.end();

                game.batch.begin();
                font.setColor(Color.CYAN);
                font.draw(game.batch, "\n\n\n* N(o)", Box_Chat_X * 1.4f + Box_Chat_WIDTH * 0.6f, Box_Chat_Y * 1.4f + Text_Chat_HEIGHT, Text_Chat_WIDTH, -1, true);
                game.batch.end();

                if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
                    isConfirmed = "true";
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                    isConfirmed = "false";
                }
            }
        }
    }

    private float timeSeconds;
    private float period;
    private String fullText = "";
    private String currentText = "";
    private int index;
    private int state;

    public void startConfirmPopUp() {
        fullText = "You found a cave, do you want to explore it?";
        timeSeconds = 0f;
        period = 0.1f;
        index = 0;
        state = 0;
        isConfirmed = "";
    }

    private void ConfirmPopUpMessage() {
        timeSeconds += Gdx.graphics.getDeltaTime();
        if(timeSeconds > period) {
            timeSeconds -= period;
            if (index < fullText.length()) {
                currentText = fullText.substring(0, index + 1);
                index++;
            } else if (index == fullText.length()) {
                index++;
            }
        }
    }

    private void ConfirmOptions() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && index < fullText.length()) {
            index = fullText.length() - 1;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && index > fullText.length()) {
            currentText = "";
            index = 0;
        }
    }
}
