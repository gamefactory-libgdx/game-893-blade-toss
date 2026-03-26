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

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/screen_mainmenu.png";

    public MainMenuScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        game.playMusic("sounds/music/music_menu.ogg");

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontBody);

        // Title label — top-Y=80, size=360x72 → libgdxY = 854-80-72 = 702
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("BLADE TOSS", titleStyle);
        titleLabel.setSize(360f, 72f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 360f) / 2f, 702f);
        stage.addActor(titleLabel);

        // PLAY — top-Y=420, size=260x60 → libgdxY=374
        TextButton playBtn = UiFactory.makeButton("PLAY", rectStyle, 260f, 60f);
        playBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 374f);
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(playBtn);

        // STAGE SELECT — top-Y=500, size=260x52 → libgdxY=302
        TextButton stageSelectBtn = UiFactory.makeButton("STAGE SELECT", rectStyle, 260f, 52f);
        stageSelectBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 302f);
        stageSelectBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(stageSelectBtn);

        // LEADERBOARD — top-Y=570, size=260x52 → libgdxY=232
        TextButton leaderboardBtn = UiFactory.makeButton("LEADERBOARD", rectStyle, 260f, 52f);
        leaderboardBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 232f);
        leaderboardBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(leaderboardBtn);

        // HOW TO PLAY — top-Y=640, size=260x52 → libgdxY=162
        TextButton howToPlayBtn = UiFactory.makeButton("HOW TO PLAY", rectStyle, 260f, 52f);
        howToPlayBtn.setPosition((Constants.WORLD_WIDTH - 260f) / 2f, 162f);
        howToPlayBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new HowToPlayScreen(game));
            }
        });
        stage.addActor(howToPlayBtn);

        // SETTINGS (round icon) — top-Y=760, right@20, size=60x60 → x=400, libgdxY=34
        TextButton settingsBtn = UiFactory.makeButton("S", roundStyle, 60f, 60f);
        settingsBtn.setPosition(400f, 34f);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new SettingsScreen(game));
            }
        });
        stage.addActor(settingsBtn);
    }

    private void registerInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
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
