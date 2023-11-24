package odyssey.game.KeyMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import odyssey.game.OdysseyGame;

public class KeyHandler {

    private static final Sound option_switch = Gdx.audio.newSound(Gdx.files.internal("sounds/option_switch.ogg"));
    private String axis;
    private float position;
    private float max;
    private float step;

    public KeyHandler(String axis, float position, float max, float step) {
        this.axis = axis;
        this.position = position;
        this.max = max;
        this.step = step;
    }

    public void KeyHandlerInput() {
        if(this.axis == "y") {
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                this.position -= step;
                option_switch.play(OdysseyGame.SE_VOLUME);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                this.position += step;
                option_switch.play(OdysseyGame.SE_VOLUME);
            }

            if(this.position > this.max) {
                this.position = 0;
            } else if (this.position < 0.0f) {
                this.position = max;
            }
        }

        if(this.axis == "x") {
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                this.position -= step;
                option_switch.play(OdysseyGame.SE_VOLUME);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                this.position += step;
                option_switch.play(OdysseyGame.SE_VOLUME);
            }

            if(this.position > this.max) {
                this.position = 0;
            } else if (this.position < 0.0f) {
                this.position = max;
            }
        }
    }

    public float getPosition() {
        return position;
    }
}
