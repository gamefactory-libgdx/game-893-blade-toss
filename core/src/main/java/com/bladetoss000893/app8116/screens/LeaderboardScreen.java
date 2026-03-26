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

public class LeaderboardScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/screen_leaderboard.png";

    // Row top-Y positions from FIGMA_BRIEF (7 rows)
    private static final float[] ROW_TOP_Y = { 200f, 270f, 340f, 410f, 480f, 550f, 620f };
    private static final float   ROW_H     = 56f;
    private static final float   ROW_W     = 400f;

    public LeaderboardScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        registerInput();
    }

    /**
     * Inserts a score into the top-{@value Constants#LEADERBOARD_SIZE} list stored in SharedPreferences.
     * Scores are kept in descending order.
     */
    public static void addScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int size = Constants.LEADERBOARD_SIZE;
        int[] scores = new int[size];
        for (int i = 0; i < size; i++) {
            scores[i] = prefs.getInteger(Constants.PREF_LEADERBOARD_PREFIX + i, 0);
        }
        // Insert in sorted descending order
        for (int i = 0; i < size; i++) {
            if (score > scores[i]) {
                for (int j = size - 1; j > i; j--) {
                    scores[j] = scores[j - 1];
                }
                scores[i] = score;
                break;
            }
        }
        for (int i = 0; i < size; i++) {
            prefs.putInteger(Constants.PREF_LEADERBOARD_PREFIX + i, scores[i]);
        }
        prefs.flush();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);

        // Title — top-Y=55, size=300x56 → libgdxY=743
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("LEADERBOARD", titleStyle);
        titleLabel.setSize(300f, 56f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 743f);
        stage.addActor(titleLabel);

        // Score rows
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        Label.LabelStyle rowStyle = new Label.LabelStyle(game.fontBody, null);

        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            int storedScore = prefs.getInteger(Constants.PREF_LEADERBOARD_PREFIX + i, 0);
            String text;
            if (storedScore > 0) {
                text = (i + 1) + ".   " + storedScore;
            } else {
                text = (i + 1) + ".   ---";
            }
            // Convert top-Y: libgdxY = WORLD_HEIGHT - topY - h
            float libgdxY = Constants.WORLD_HEIGHT - ROW_TOP_Y[i] - ROW_H;
            Label rowLabel = new Label(text, rowStyle);
            rowLabel.setSize(ROW_W, ROW_H);
            rowLabel.setAlignment(Align.center);
            rowLabel.setPosition((Constants.WORLD_WIDTH - ROW_W) / 2f, libgdxY);
            stage.addActor(rowLabel);
        }

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

        // SHARE — top-Y=774, right@20, size=120x44 → x=340, libgdxY=36
        TextButton shareBtn = UiFactory.makeButton("SHARE", smallStyle, 120f, 44f);
        shareBtn.setPosition(Constants.WORLD_WIDTH - 20f - 120f, 36f);
        shareBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                // Share functionality is platform-specific; no-op on base implementation
            }
        });
        stage.addActor(shareBtn);
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

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
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
