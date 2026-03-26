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

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;
    private final int      score;

    private static final String BG = "ui/screen_gameover.png";

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game  = game;
        this.score = score;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        // Save score to leaderboard and update high score
        LeaderboardScreen.addScore(score);
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int prevBest = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > prevBest) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }

        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);

        // GAME OVER — top-Y=160, size=320x72 → libgdxY=622
        Label gameOverLabel = new Label("GAME OVER", titleStyle);
        gameOverLabel.setSize(320f, 72f);
        gameOverLabel.setAlignment(Align.center);
        gameOverLabel.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 622f);
        stage.addActor(gameOverLabel);

        // SCORE — top-Y=310, size=260x52 → libgdxY=492
        Label scoreLabel = new Label("SCORE: " + score, scoreStyle);
        scoreLabel.setSize(260f, 52f);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 492f);
        stage.addActor(scoreLabel);

        // BEST — top-Y=375, size=240x44 → libgdxY=435
        int bestScore = Gdx.app.getPreferences(Constants.PREFS_NAME)
                .getInteger(Constants.PREF_HIGH_SCORE, score);
        Label bestLabel = new Label("BEST: " + bestScore, bodyStyle);
        bestLabel.setSize(240f, 44f);
        bestLabel.setAlignment(Align.center);
        bestLabel.setPosition((Constants.WORLD_WIDTH - 240f) / 2f, 435f);
        stage.addActor(bestLabel);

        // RETRY — top-Y=510, size=260x60 → libgdxY=284
        TextButton retryBtn = UiFactory.makeButton("RETRY", rectStyle, 260f, 60f);
        retryBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 284f);
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, 1));
            }
        });
        stage.addActor(retryBtn);

        // STAGE SELECT — top-Y=590, size=260x52 → libgdxY=212
        TextButton stageSelectBtn = UiFactory.makeButton("STAGE SELECT", rectStyle, 260f, 52f);
        stageSelectBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 212f);
        stageSelectBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(stageSelectBtn);

        // MAIN MENU — top-Y=670, size=260x52 → libgdxY=132
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", smallStyle, 260f, 52f);
        menuBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 132f);
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
