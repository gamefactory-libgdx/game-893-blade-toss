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

public class StageClearScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;
    private final int      stageNumber;
    private final int      score;

    private static final String BG = "ui/screen_stageclear.png";

    public StageClearScreen(MainGame game, int stageNumber, int score, int appleBonus) {
        this.game        = game;
        this.stageNumber = stageNumber;
        this.score       = score;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        // Unlock next stage
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int unlocked = prefs.getInteger(Constants.PREF_UNLOCKED_STAGES, 1);
        if (stageNumber >= unlocked) {
            prefs.putInteger(Constants.PREF_UNLOCKED_STAGES,
                    Math.min(stageNumber + 1, Constants.TOTAL_STAGES));
            prefs.flush();
        }
        // Update high score
        int prevBest = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > prevBest) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }

        // Add to leaderboard
        LeaderboardScreen.addScore(score);

        game.playMusic("sounds/music/music_menu.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_level_complete.ogg", Sound.class).play(1.0f);

        buildUI(appleBonus);
        registerInput();
    }

    private void buildUI(int appleBonus) {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);

        // STAGE CLEAR! — top-Y=180, h=64 → libgdxY=610
        Label clearLabel = new Label("STAGE " + stageNumber + " CLEAR!", titleStyle);
        clearLabel.setSize(340f, 64f);
        clearLabel.setAlignment(Align.center);
        clearLabel.setPosition((Constants.WORLD_WIDTH - 340f) / 2f, 610f);
        stage.addActor(clearLabel);

        // SCORE — top-Y=280, h=52 → libgdxY=522
        Label scoreLabel = new Label("SCORE: " + score, scoreStyle);
        scoreLabel.setSize(280f, 52f);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - 280f) / 2f, 522f);
        stage.addActor(scoreLabel);

        // APPLE BONUS — top-Y=350, h=44 → libgdxY=460
        if (appleBonus > 0) {
            Label bonusLabel = new Label("APPLE BONUS: +" + appleBonus, bodyStyle);
            bonusLabel.setSize(240f, 44f);
            bonusLabel.setAlignment(Align.center);
            bonusLabel.setPosition((Constants.WORLD_WIDTH - 240f) / 2f, 460f);
            stage.addActor(bonusLabel);
        }

        // NEXT STAGE / ALL CLEAR — top-Y=500, h=60 → libgdxY=294
        if (stageNumber < Constants.TOTAL_STAGES) {
            TextButton nextBtn = UiFactory.makeButton("NEXT STAGE", rectStyle,
                    Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
            nextBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 294f);
            nextBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    playClick();
                    game.setScreen(new GameScreen(game, stageNumber + 1));
                }
            });
            stage.addActor(nextBtn);
        } else {
            Label allClearLabel = new Label("ALL STAGES COMPLETE!", bodyStyle);
            allClearLabel.setSize(Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
            allClearLabel.setAlignment(Align.center);
            allClearLabel.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 294f);
            stage.addActor(allClearLabel);
        }

        // RETRY — top-Y=580, h=52 → libgdxY=222
        TextButton retryBtn = UiFactory.makeButton("RETRY", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_SECONDARY);
        retryBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 222f);
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, stageNumber));
            }
        });
        stage.addActor(retryBtn);

        // MAIN MENU — top-Y=660, h=52 → libgdxY=142
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", smallStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_SECONDARY);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 142f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
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

    @Override public void show() { registerInput(); }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
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
