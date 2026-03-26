package com.bladetoss000893.app8116.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

public class PauseScreen implements Screen {

    private final MainGame game;
    private final Screen   previousScreen;
    private final Stage    stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapes;

    private static final String BG = "ui/screen_game.png";

    public PauseScreen(MainGame game, Screen previousScreen) {
        this.game           = game;
        this.previousScreen = previousScreen;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.update();
        stage    = new Stage(viewport, game.batch);
        shapes   = new ShapeRenderer();

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);

        // PAUSED title
        Label pauseLabel = new Label("PAUSED", titleStyle);
        pauseLabel.setSize(280f, 64f);
        pauseLabel.setAlignment(Align.center);
        pauseLabel.setPosition((Constants.WORLD_WIDTH - 280f) / 2f, 660f);
        stage.addActor(pauseLabel);

        // RESUME — centered ~530
        TextButton resumeBtn = UiFactory.makeButton("RESUME", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        resumeBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 520f);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(previousScreen);
            }
        });
        stage.addActor(resumeBtn);

        // RESTART — centered ~440
        TextButton restartBtn = UiFactory.makeButton("RESTART", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_SECONDARY);
        restartBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 440f);
        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                int sn = (previousScreen instanceof GameScreen)
                        ? ((GameScreen) previousScreen).getStageNumber() : 1;
                game.setScreen(new GameScreen(game, sn));
            }
        });
        stage.addActor(restartBtn);

        // MAIN MENU — centered ~370
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_SECONDARY);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 360f);
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
                    game.setScreen(previousScreen);
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

        // Game background
        game.batch.begin();
        game.batch.draw(game.manager.get(BG, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // Dark semi-transparent overlay
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0f, 0f, 0f, 0.7f);
        shapes.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); shapes.dispose(); }
}
