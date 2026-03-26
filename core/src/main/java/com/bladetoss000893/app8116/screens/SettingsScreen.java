package com.bladetoss000893.app8116.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bladetoss000893.app8116.Constants;
import com.bladetoss000893.app8116.MainGame;
import com.bladetoss000893.app8116.UiFactory;

public class SettingsScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/screen_settings.png";

    private TextButton musicToggleBtn;
    private TextButton sfxToggleBtn;
    private TextButton vibrationToggleBtn;
    private TextButton difficultyBtn;

    public SettingsScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);

        // Title label — top-Y=80, size=280x56 → libgdxY=718
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("SETTINGS", titleStyle);
        titleLabel.setSize(280f, 56f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 280f) / 2f, 718f);
        stage.addActor(titleLabel);

        // SOUND (SFX) toggle — top-Y=250, size=320x56 → libgdxY=548
        sfxToggleBtn = UiFactory.makeButton(sfxLabel(), rectStyle, 320f, 56f);
        sfxToggleBtn.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 548f);
        sfxToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                game.sfxEnabled = !game.sfxEnabled;
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                sfxToggleBtn.setText(sfxLabel());
                playToggle();
            }
        });
        stage.addActor(sfxToggleBtn);

        // MUSIC toggle — top-Y=330, size=320x56 → libgdxY=468
        musicToggleBtn = UiFactory.makeButton(musicLabel(), rectStyle, 320f, 56f);
        musicToggleBtn.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 468f);
        musicToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                game.musicEnabled = !game.musicEnabled;
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else game.currentMusic.pause();
                }
                musicToggleBtn.setText(musicLabel());
                playToggle();
            }
        });
        stage.addActor(musicToggleBtn);

        // VIBRATION toggle — top-Y=410, size=320x56 → libgdxY=388
        vibrationToggleBtn = UiFactory.makeButton(vibrationLabel(), rectStyle, 320f, 56f);
        vibrationToggleBtn.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 388f);
        vibrationToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                game.vibrationEnabled = !game.vibrationEnabled;
                prefs.putBoolean(Constants.PREF_VIBRATION, game.vibrationEnabled);
                prefs.flush();
                vibrationToggleBtn.setText(vibrationLabel());
                playToggle();
            }
        });
        stage.addActor(vibrationToggleBtn);

        // DIFFICULTY selector — top-Y=490, size=320x56 → libgdxY=308
        difficultyBtn = UiFactory.makeButton(difficultyLabel(), rectStyle, 320f, 56f);
        difficultyBtn.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 308f);
        difficultyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                int diff = prefs.getInteger(Constants.PREF_DIFFICULTY, Constants.DIFFICULTY_NORMAL);
                diff = (diff + 1) % 3;
                prefs.putInteger(Constants.PREF_DIFFICULTY, diff);
                prefs.flush();
                difficultyBtn.setText(difficultyLabel());
                playToggle();
            }
        });
        stage.addActor(difficultyBtn);

        // BACK — top-Y=774, left@20, size=120x44 → x=20, libgdxY=36
        TextButton backBtn = UiFactory.makeButton("BACK", smallStyle, 120f, 44f);
        backBtn.setPosition(20f, 36f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
    }

    private String sfxLabel() {
        return "SOUND: " + (game.sfxEnabled ? "ON" : "OFF");
    }

    private String musicLabel() {
        return "MUSIC: " + (game.musicEnabled ? "ON" : "OFF");
    }

    private String vibrationLabel() {
        return "VIBRATION: " + (game.vibrationEnabled ? "ON" : "OFF");
    }

    private String difficultyLabel() {
        int diff = Gdx.app.getPreferences(Constants.PREFS_NAME)
                .getInteger(Constants.PREF_DIFFICULTY, Constants.DIFFICULTY_NORMAL);
        switch (diff) {
            case Constants.DIFFICULTY_EASY:   return "DIFFICULTY: EASY";
            case Constants.DIFFICULTY_HARD:   return "DIFFICULTY: HARD";
            default:                          return "DIFFICULTY: NORMAL";
        }
    }

    private void registerInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));
    }

    private void playToggle() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_toggle.ogg", Sound.class).play(0.5f);
    }

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    @Override public void show()   { registerInput(); }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(game.manager.get(BG, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); }
}
