package com.bladetoss000893.app8116;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.bladetoss000893.app8116.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch  batch;
    public AssetManager manager;

    // Fonts
    public BitmapFont fontTitle;
    public BitmapFont fontBody;
    public BitmapFont fontSmall;
    public BitmapFont fontScore;

    // Audio state
    public boolean musicEnabled     = true;
    public boolean sfxEnabled       = true;
    public boolean vibrationEnabled = true;
    public Music   currentMusic     = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        // Load saved prefs
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicEnabled     = prefs.getBoolean(Constants.PREF_MUSIC,     true);
        sfxEnabled       = prefs.getBoolean(Constants.PREF_SFX,       true);
        vibrationEnabled = prefs.getBoolean(Constants.PREF_VIBRATION, true);

        generateFonts();
        loadCoreAssets();

        setScreen(new MainMenuScreen(this));
    }

    private void generateFonts() {
        // Assigned fonts: Toxigenesis.otf (title), Ferrum.otf (body)
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toxigenesis.otf"));
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Ferrum.otf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.borderColor = new Color(0f, 0f, 0f, 0.85f);

        // Title font — large
        p.size        = Constants.FONT_SIZE_TITLE;
        p.borderWidth = 3;
        fontTitle = titleGen.generateFont(p);

        // Score font
        p.size        = Constants.FONT_SIZE_SCORE;
        p.borderWidth = 3;
        fontScore = titleGen.generateFont(p);

        // Body font
        p.size        = Constants.FONT_SIZE_BODY;
        p.borderWidth = 2;
        fontBody = bodyGen.generateFont(p);

        // Small font
        p.size        = Constants.FONT_SIZE_SMALL;
        p.borderWidth = 2;
        fontSmall = bodyGen.generateFont(p);

        titleGen.dispose();
        bodyGen.dispose();
    }

    private void loadCoreAssets() {
        // UI buttons
        manager.load("ui/buttons/button_rectangle_depth_gradient.png", Texture.class);
        manager.load("ui/buttons/button_rectangle_depth_flat.png",     Texture.class);
        manager.load("ui/buttons/button_round_depth_gradient.png",     Texture.class);
        manager.load("ui/buttons/button_round_depth_flat.png",         Texture.class);
        manager.load("ui/buttons/star.png",                            Texture.class);
        manager.load("ui/buttons/star_outline.png",                    Texture.class);

        // UI screen backgrounds
        manager.load("ui/screen_mainmenu.png",    Texture.class);
        manager.load("ui/screen_stageselect.png", Texture.class);
        manager.load("ui/screen_game.png",        Texture.class);
        manager.load("ui/screen_stageclear.png",  Texture.class);
        manager.load("ui/screen_gameover.png",    Texture.class);
        manager.load("ui/screen_leaderboard.png", Texture.class);
        manager.load("ui/screen_settings.png",    Texture.class);
        manager.load("ui/screen_howtoplay.png",   Texture.class);

        // Music
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        manager.load("sounds/sfx/sfx_shoot.ogg",          Sound.class);
        manager.load("sounds/sfx/sfx_coin.ogg",           Sound.class);

        manager.finishLoading();
    }

    // --- Music helpers ---

    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        fontTitle.dispose();
        fontBody.dispose();
        fontSmall.dispose();
        fontScore.dispose();
    }
}
