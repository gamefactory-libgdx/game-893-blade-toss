package com.bladetoss000893.app8116.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

public class StageSelectScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/screen_stageselect.png";

    // FIGMA_BRIEF row positions (top-Y px, h=90) → libgdxY = 854 - topY - 90
    private static final float[] ROW_LIBGDX_Y = {
        854f - 180f - 90f,   // row 0 → 584 (stages 1,2)
        854f - 290f - 90f,   // row 1 → 474 (stages 3,4)
        854f - 400f - 90f,   // row 2 → 364 (stages 5,6)
        854f - 510f - 90f,   // row 3 → 254 (stages 7,8)
        854f - 620f - 90f,   // row 4 → 144 (stages 9,10)
    };

    private static final float LEFT_X  = 40f;
    private static final float RIGHT_X = Constants.WORLD_WIDTH - 40f - Constants.BTN_W_STAGE; // 260

    public StageSelectScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        game.playMusic("sounds/music/music_menu.ogg");

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle stageStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);

        // Title — top-Y=60, h=56 → libgdxY=738
        Label titleLabel = new Label("SELECT STAGE", titleStyle);
        titleLabel.setSize(300f, 56f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 738f);
        stage.addActor(titleLabel);

        // Read unlocked stages
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        final int unlockedStages = prefs.getInteger(Constants.PREF_UNLOCKED_STAGES, 1);

        // 10 stage buttons in 2x5 grid
        for (int i = 0; i < 10; i++) {
            final int stageNum = i + 1;
            int row = i / 2;
            boolean isLeft = (i % 2 == 0);
            float bx = isLeft ? LEFT_X : RIGHT_X;
            float by = ROW_LIBGDX_Y[row];

            boolean unlocked = stageNum <= unlockedStages;

            String label = unlocked ? "STAGE " + stageNum : "STAGE " + stageNum + "\nLOCKED";
            TextButton btn = UiFactory.makeButton(label, stageStyle,
                    Constants.BTN_W_STAGE, Constants.BTN_H_STAGE);
            btn.setPosition(bx, by);
            btn.getLabel().setAlignment(Align.center);

            if (!unlocked) {
                btn.getLabel().setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
            } else {
                btn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        playClick();
                        game.setScreen(new GameScreen(game, stageNum));
                    }
                });
            }
            stage.addActor(btn);
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
