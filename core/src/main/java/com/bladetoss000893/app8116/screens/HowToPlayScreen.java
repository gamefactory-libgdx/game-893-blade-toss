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

public class HowToPlayScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/screen_howtoplay.png";

    // Instruction lines — converted from FIGMA_BRIEF top-Y coords
    // libgdxY = WORLD_HEIGHT - topY - elementHeight (height=40 per line)
    private static final String[] INSTRUCTIONS = {
        "TAP the screen to throw a blade",
        "Blades MUST NOT touch each other",
        "Embed ALL blades to clear the stage",
        "Hit the APPLE for a bonus 50 points",
        "Log spins FASTER in later stages",
        "Stages alternate spin direction",
        "Master all 10 stages to win!"
    };

    public HowToPlayScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        game.playMusic("sounds/music/music_menu.ogg");

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);

        // HOW TO PLAY title — top-Y=55, h=56 → libgdxY=743
        Label titleLabel = new Label("HOW TO PLAY", titleStyle);
        titleLabel.setSize(300f, 56f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 743f);
        stage.addActor(titleLabel);

        // Instruction lines — top-Y: 180, 240, 300, 360, 420, 480, 540 → libgdxY = 854 - topY - 40
        float[] lineTopY = { 180f, 240f, 300f, 360f, 420f, 480f, 540f };
        for (int i = 0; i < INSTRUCTIONS.length && i < lineTopY.length; i++) {
            float libgdxY = Constants.WORLD_HEIGHT - lineTopY[i] - 40f;
            Label line = new Label(INSTRUCTIONS[i], bodyStyle);
            line.setSize(400f, 40f);
            line.setAlignment(Align.center);
            line.setWrap(true);
            line.setPosition((Constants.WORLD_WIDTH - 400f) / 2f, libgdxY);
            stage.addActor(line);
        }

        // BACK button — top-Y=774, h=44 → libgdxY=36, x=20
        TextButton backBtn = UiFactory.makeButton("BACK", smallStyle,
                Constants.BTN_W_SMALL, Constants.BTN_H_SMALL);
        backBtn.setPosition(20f, 36f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
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
