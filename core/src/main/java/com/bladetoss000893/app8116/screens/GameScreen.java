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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

public class GameScreen implements Screen {

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------
    private static final int   STATE_WAITING       = 0;
    private static final int   STATE_FLYING        = 1;
    private static final int   STATE_EMBED_DELAY   = 2;
    private static final int   STATE_GAMEOVER      = 3;
    private static final int   STATE_STAGECLEAR    = 4;

    private static final float EMBED_DELAY_SEC     = 0.18f;
    private static final float GAMEOVER_DELAY_SEC  = 0.9f;
    private static final float STAGECLEAR_DELAY_SEC = 1.1f;
    private static final float MIN_BLADE_ANGLE_DEG = 22f;  // minimum degrees between blades
    private static final float APPLE_HIT_ANGLE_DEG = 20f;

    private static final String APPLE_TEX  = "sprites/object/coinGold.png";
    private static final String BG_TEX     = "ui/screen_game.png";

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------
    private final MainGame       game;
    private final int            stageNumber;
    private final int            totalBlades;
    private final float          rotationSpeed;   // deg/sec, negative = CCW

    private final Stage          hudStage;
    private final Viewport       viewport;
    private final OrthographicCamera camera;
    private final ShapeRenderer  shapes;

    // Log & blade state
    private float   logAngle       = 0f;
    private float[] embeddedLocalAngles;
    private int     embeddedCount  = 0;
    private int     gameState      = STATE_WAITING;
    private float   stateTimer     = 0f;
    private float   bladeY         = Constants.BLADE_LAUNCH_Y;

    // Score
    private int     score          = 0;
    private int     appleBonusTotal = 0;

    // Apple bonus
    private float   appleLocalAngle;
    private boolean applePresent   = true;

    // HUD actors
    private Label scoreLabel;
    private Label bladesLabel;
    private Label statusLabel;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    public GameScreen(MainGame game, int stageNumber) {
        this.game        = game;
        this.stageNumber = stageNumber;

        // Stage 1-10: 5..9 blades (adds 0.5 per stage rounded down)
        this.totalBlades  = Constants.BASE_BLADES_PER_STAGE + (stageNumber - 1) / 2;

        // Odd stages CW, even stages CCW, speed increases each stage
        float speed = Constants.LOG_ROTATION_SPEED
                + (stageNumber - 1) * Constants.LOG_ROTATION_INCREASE;
        this.rotationSpeed = (stageNumber % 2 == 0) ? -speed : speed;

        this.embeddedLocalAngles = new float[totalBlades];
        this.appleLocalAngle     = MathUtils.random(0f, 360f);

        // Load apple texture if not yet loaded
        if (!game.manager.isLoaded(APPLE_TEX)) {
            game.manager.load(APPLE_TEX, Texture.class);
            game.manager.finishLoading();
        }

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.update();
        hudStage = new Stage(viewport, game.batch);
        shapes   = new ShapeRenderer();

        game.playMusic("sounds/music/music_gameplay.ogg");

        buildHUD();
        registerInput();
    }

    /** Called by PauseScreen to restart the same stage. */
    public int getStageNumber() { return stageNumber; }

    // -----------------------------------------------------------------------
    // HUD
    // -----------------------------------------------------------------------
    private void buildHUD() {
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, null);

        // PAUSE button — top-Y=20, size=60x60 → libgdxY=774, x=20
        final GameScreen self = this;
        TextButton pauseBtn = UiFactory.makeButton("||", roundStyle,
                Constants.BTN_W_ROUND, Constants.BTN_H_ROUND);
        pauseBtn.setPosition(20f, 774f);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.setScreen(new PauseScreen(game, self));
            }
        });
        hudStage.addActor(pauseBtn);

        // SCORE — top-Y=30, size=200x48 → libgdxY=776, centered
        scoreLabel = new Label("SCORE: 0", scoreStyle);
        scoreLabel.setSize(200f, 48f);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - 200f) / 2f, 776f);
        hudStage.addActor(scoreLabel);

        // STAGE — top-Y=30, size=120x44 → libgdxY=780, right@20
        Label stageLabel = new Label("STAGE " + stageNumber, bodyStyle);
        stageLabel.setSize(120f, 44f);
        stageLabel.setAlignment(Align.center);
        stageLabel.setPosition(Constants.WORLD_WIDTH - 20f - 120f, 780f);
        hudStage.addActor(stageLabel);

        // BLADES REMAINING — top-Y=80, size=180x36 → libgdxY=738, centered
        bladesLabel = new Label(bladesText(), smallStyle);
        bladesLabel.setSize(180f, 36f);
        bladesLabel.setAlignment(Align.center);
        bladesLabel.setPosition((Constants.WORLD_WIDTH - 180f) / 2f, 738f);
        hudStage.addActor(bladesLabel);

        // STATUS label (collision / stage clear flash) — centered vertically
        // fontColor = WHITE so actor.setColor() controls the final displayed color
        statusLabel = new Label("", new Label.LabelStyle(game.fontTitle, Color.WHITE));
        statusLabel.setSize(400f, 64f);
        statusLabel.setAlignment(Align.center);
        statusLabel.setPosition((Constants.WORLD_WIDTH - 400f) / 2f, 380f);
        statusLabel.setVisible(false);
        hudStage.addActor(statusLabel);
    }

    private String bladesText() {
        int remaining = totalBlades - embeddedCount;
        return "BLADES: " + remaining + "/" + totalBlades;
    }

    // -----------------------------------------------------------------------
    // Input
    // -----------------------------------------------------------------------
    private void registerInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                launchBlade();
                return true;
            }
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));
    }

    private void launchBlade() {
        if (gameState != STATE_WAITING) return;
        gameState = STATE_FLYING;
        bladeY    = Constants.BLADE_LAUNCH_Y;
        playSound("sounds/sfx/sfx_shoot.ogg", 0.75f);
    }

    // -----------------------------------------------------------------------
    // Update
    // -----------------------------------------------------------------------
    private void update(float delta) {
        logAngle = (logAngle + rotationSpeed * delta) % 360f;

        switch (gameState) {
            case STATE_WAITING:
                break;

            case STATE_FLYING:
                bladeY += Constants.BLADE_LAUNCH_SPEED * delta;
                float bladeTip = bladeY + Constants.BLADE_HEIGHT * 0.5f;
                if (bladeTip >= Constants.LOG_CENTER_Y - Constants.LOG_RADIUS) {
                    handleBladeLanding();
                }
                break;

            case STATE_EMBED_DELAY:
                stateTimer -= delta;
                if (stateTimer <= 0f) {
                    gameState = STATE_WAITING;
                    bladeY    = Constants.BLADE_LAUNCH_Y;
                    statusLabel.setVisible(false);
                }
                break;

            case STATE_GAMEOVER:
                stateTimer -= delta;
                if (stateTimer <= 0f) {
                    game.setScreen(new GameOverScreen(game, score, 0));
                }
                break;

            case STATE_STAGECLEAR:
                stateTimer -= delta;
                if (stateTimer <= 0f) {
                    game.setScreen(new StageClearScreen(game, stageNumber, score, appleBonusTotal));
                }
                break;
        }
    }

    private void handleBladeLanding() {
        // Compute local angle on the log at time of impact
        // Blade always arrives from below (world angle 270°)
        float localAngle = ((270f - logAngle) % 360f + 360f) % 360f;

        // Check collision with already-embedded blades
        for (int i = 0; i < embeddedCount; i++) {
            float diff = Math.abs(localAngle - embeddedLocalAngles[i]) % 360f;
            if (diff > 180f) diff = 360f - diff;
            if (diff < MIN_BLADE_ANGLE_DEG) {
                // COLLISION — game over
                playSound("sounds/sfx/sfx_hit.ogg", 1.0f);
                statusLabel.setText("COLLISION!");
                statusLabel.setColor(Color.RED);
                statusLabel.setVisible(true);
                gameState  = STATE_GAMEOVER;
                stateTimer = GAMEOVER_DELAY_SEC;
                if (game.vibrationEnabled) {
                    try { Gdx.input.vibrate(80); } catch (Exception ignored) {}
                }
                return;
            }
        }

        // No collision — embed the blade
        embeddedLocalAngles[embeddedCount++] = localAngle;
        score += Constants.SCORE_BLADE_HIT;

        // Check apple bonus
        if (applePresent) {
            float appleDiff = Math.abs(localAngle - appleLocalAngle) % 360f;
            if (appleDiff > 180f) appleDiff = 360f - appleDiff;
            if (appleDiff < APPLE_HIT_ANGLE_DEG) {
                score += Constants.SCORE_APPLE_BONUS;
                appleBonusTotal += Constants.SCORE_APPLE_BONUS;
                applePresent = false;
                playSound("sounds/sfx/sfx_coin.ogg", 1.0f);
            }
        }

        scoreLabel.setText("SCORE: " + score);
        bladesLabel.setText(bladesText());

        if (embeddedCount >= totalBlades) {
            // Stage cleared!
            score += Constants.SCORE_STAGE_CLEAR_BONUS;
            scoreLabel.setText("SCORE: " + score);
            playSound("sounds/sfx/sfx_level_complete.ogg", 1.0f);
            statusLabel.setText("STAGE CLEAR!");
            statusLabel.setColor(new Color(0.95f, 0.82f, 0.22f, 1f));
            statusLabel.setVisible(true);
            gameState  = STATE_STAGECLEAR;
            stateTimer = STAGECLEAR_DELAY_SEC;
        } else {
            gameState  = STATE_EMBED_DELAY;
            stateTimer = EMBED_DELAY_SEC;
        }
    }

    // -----------------------------------------------------------------------
    // Render
    // -----------------------------------------------------------------------
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. Background image
        game.batch.begin();
        game.batch.draw(game.manager.get(BG_TEX, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // 2. Log body (filled circle)
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawLogFilled();
        drawEmbeddedBladesAll();
        if (gameState == STATE_FLYING) {
            drawBlade(bladeY);
        } else if (gameState == STATE_WAITING) {
            drawBlade(Constants.BLADE_LAUNCH_Y);
        }
        shapes.end();

        // 3. Log grain rings (line mode)
        shapes.begin(ShapeRenderer.ShapeType.Line);
        drawLogGrain();
        shapes.end();

        // 4. Apple sprite
        if (applePresent) {
            drawApple();
        }

        // 5. Flash overlay for game-over / stage-clear
        drawFlashOverlay();

        // 6. HUD
        hudStage.act(delta);
        hudStage.draw();
    }

    // -----------------------------------------------------------------------
    // Drawing helpers
    // -----------------------------------------------------------------------

    private void drawLogFilled() {
        // Log body — dark brown
        shapes.setColor(0.27f, 0.17f, 0.08f, 1f);
        shapes.circle(Constants.LOG_CENTER_X, Constants.LOG_CENTER_Y, Constants.LOG_RADIUS, 64);
        // Center knot
        shapes.setColor(0.17f, 0.10f, 0.05f, 1f);
        shapes.circle(Constants.LOG_CENTER_X, Constants.LOG_CENTER_Y,
                Constants.LOG_RADIUS * 0.11f, 24);
    }

    private void drawLogGrain() {
        float cx = Constants.LOG_CENTER_X;
        float cy = Constants.LOG_CENTER_Y;
        float r  = Constants.LOG_RADIUS;
        // Concentric grain rings
        shapes.setColor(0.50f, 0.33f, 0.16f, 0.75f);
        for (float ri = r * 0.22f; ri < r * 0.94f; ri += r * 0.18f) {
            shapes.circle(cx, cy, ri, 48);
        }
        // Radial grain lines
        shapes.setColor(0.40f, 0.26f, 0.12f, 0.45f);
        for (int i = 0; i < 8; i++) {
            float a = (logAngle + i * 45f) * MathUtils.degreesToRadians;
            shapes.line(cx, cy,
                    cx + MathUtils.cos(a) * r * 0.88f,
                    cy + MathUtils.sin(a) * r * 0.88f);
        }
    }

    private void drawEmbeddedBladesAll() {
        for (int i = 0; i < embeddedCount; i++) {
            float worldAngle = ((logAngle + embeddedLocalAngles[i]) % 360f + 360f) % 360f;
            drawEmbeddedBlade(worldAngle);
        }
    }

    private void drawEmbeddedBlade(float worldAngleDeg) {
        float rad  = worldAngleDeg * MathUtils.degreesToRadians;
        float cosA = MathUtils.cos(rad);
        float sinA = MathUtils.sin(rad);

        float surfX = Constants.LOG_CENTER_X + cosA * Constants.LOG_RADIUS;
        float surfY = Constants.LOG_CENTER_Y + sinA * Constants.LOG_RADIUS;

        // Tip (embedded inside log)
        float tipX = surfX - cosA * Constants.BLADE_HEIGHT * 0.32f;
        float tipY = surfY - sinA * Constants.BLADE_HEIGHT * 0.32f;
        // Handle end (sticking out)
        float hndX = surfX + cosA * Constants.BLADE_HEIGHT * 0.68f;
        float hndY = surfY + sinA * Constants.BLADE_HEIGHT * 0.68f;
        // Handle-guard split
        float grdX = surfX + cosA * Constants.BLADE_HEIGHT * 0.28f;
        float grdY = surfY + sinA * Constants.BLADE_HEIGHT * 0.28f;

        // Blade body (steel)
        shapes.setColor(0.62f, 0.70f, 0.76f, 1f);
        shapes.rectLine(tipX, tipY, grdX, grdY, Constants.BLADE_WIDTH * 0.42f);
        // Handle (wood brown)
        shapes.setColor(0.38f, 0.23f, 0.11f, 1f);
        shapes.rectLine(grdX, grdY, hndX, hndY, Constants.BLADE_WIDTH * 0.58f);
    }

    private void drawBlade(float centerY) {
        float cx     = Constants.BLADE_LAUNCH_X;
        float bottom = centerY - Constants.BLADE_HEIGHT * 0.38f;
        float top    = centerY + Constants.BLADE_HEIGHT * 0.62f;
        float guard  = centerY + Constants.BLADE_HEIGHT * 0.08f;

        // Blade body (steel, upper portion)
        shapes.setColor(0.62f, 0.70f, 0.76f, 1f);
        shapes.rectLine(cx, guard, cx, top, Constants.BLADE_WIDTH * 0.42f);
        // Handle (wood, lower portion)
        shapes.setColor(0.38f, 0.23f, 0.11f, 1f);
        shapes.rectLine(cx, bottom, cx, guard, Constants.BLADE_WIDTH * 0.60f);
    }

    private void drawApple() {
        float worldAngle = ((logAngle + appleLocalAngle) % 360f + 360f) % 360f;
        float rad  = worldAngle * MathUtils.degreesToRadians;
        float ax   = Constants.LOG_CENTER_X + MathUtils.cos(rad) * Constants.LOG_RADIUS;
        float ay   = Constants.LOG_CENTER_Y + MathUtils.sin(rad) * Constants.LOG_RADIUS;
        float size = 28f;
        game.batch.begin();
        game.batch.draw(game.manager.get(APPLE_TEX, Texture.class),
                ax - size / 2f, ay - size / 2f, size, size);
        game.batch.end();
    }

    private void drawFlashOverlay() {
        if (gameState == STATE_GAMEOVER) {
            // Red flash that fades in
            float progress = 1f - stateTimer / GAMEOVER_DELAY_SEC;
            float alpha    = 0.45f * progress;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapes.setProjectionMatrix(camera.combined);
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            shapes.setColor(0.7f, 0f, 0f, alpha);
            shapes.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
            shapes.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else if (gameState == STATE_STAGECLEAR) {
            // Golden flash
            float progress = 1f - stateTimer / STAGECLEAR_DELAY_SEC;
            float alpha    = 0.38f * progress;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapes.setProjectionMatrix(camera.combined);
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            shapes.setColor(0.92f, 0.78f, 0.15f, alpha);
            shapes.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
            shapes.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    // -----------------------------------------------------------------------
    // Sound helper
    // -----------------------------------------------------------------------
    private void playSound(String path, float volume) {
        if (game.sfxEnabled)
            game.manager.get(path, Sound.class).play(volume);
    }

    // -----------------------------------------------------------------------
    // Screen lifecycle
    // -----------------------------------------------------------------------
    @Override
    public void show() {
        registerInput();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        hudStage.dispose();
        shapes.dispose();
    }
}
