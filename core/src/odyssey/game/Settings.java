package odyssey.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public static int HEIGHT;
    public static int SIZE;
    public static float BGM_VOLUME;
    public static float SE_VOLUME;
    public static boolean SAFE_MODE;

    Preferences settings = Gdx.app.getPreferences("Odyssey.game.options");

    public void loadSettings() {
        String HEIGHT = settings.getString("HEIGHT", "");
        String SIZE = settings.getString("SIZE", "");
        String BGM_VOLUME = settings.getString("BGM_VOLUME", "");
        String SE_VOLUME = settings.getString("SE_VOLUME", "");
        String SAFE_MODE = settings.getString("SAFE_MODE", "");

        if(HEIGHT == "") {
            HEIGHT = String.valueOf(720);
            settings.putString("HEIGHT", HEIGHT);
            settings.flush();
        }

        if(SIZE == "") {
            SIZE = String.valueOf(3);
            settings.putString("SIZE", SIZE);
            settings.flush();
        }

        if(BGM_VOLUME == "") {
            BGM_VOLUME = String.valueOf(1.0f);
            settings.putString("BGM_VOLUME", BGM_VOLUME);
            settings.flush();
        }

        if(SE_VOLUME == "") {
            SE_VOLUME = String.valueOf(1.0f);
            settings.putString("SE_VOLUME", SE_VOLUME);
            settings.flush();
        }

        if(SAFE_MODE == "") {
            SAFE_MODE = String.valueOf(true);
            settings.putString("SAFE_MODE", SAFE_MODE);
            settings.flush();
        }

        Settings.HEIGHT = Integer.parseInt(HEIGHT);
        Settings.SIZE = Integer.parseInt(SIZE);
        Settings.BGM_VOLUME = Float.parseFloat(BGM_VOLUME);
        Settings.SE_VOLUME = Float.parseFloat(SE_VOLUME);
        Settings.SAFE_MODE = Boolean.parseBoolean(SAFE_MODE);
    }

    public void setSettings() {
        settings.putString("HEIGHT", String.valueOf(OdysseyGame.HEIGHT));
        settings.putString("SIZE", String.valueOf(OdysseyGame.SIZE));
        settings.putString("BGM_VOLUME", String.valueOf(OdysseyGame.BGM_VOLUME));
        settings.putString("SE_VOLUME", String.valueOf(OdysseyGame.SE_VOLUME));
        settings.putString("SAFE_MODE", String.valueOf(OdysseyGame.SAFE_MODE));
        settings.flush();
    }


}
