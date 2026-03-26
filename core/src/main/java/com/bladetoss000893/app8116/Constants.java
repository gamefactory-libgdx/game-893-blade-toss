package com.bladetoss000893.app8116;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Physics
    public static final float BLADE_LAUNCH_SPEED   = 900f;
    public static final float LOG_ROTATION_SPEED    = 60f;   // degrees per second (base)
    public static final float LOG_ROTATION_INCREASE = 5f;    // per stage
    public static final float BLADE_RADIUS          = 20f;   // collision radius for blades stuck in log
    public static final float APPLE_RADIUS          = 18f;

    // Game dimensions
    public static final float LOG_RADIUS            = 110f;
    public static final float LOG_CENTER_X          = WORLD_WIDTH / 2f;
    public static final float LOG_CENTER_Y          = 480f;
    public static final float BLADE_LAUNCH_X        = WORLD_WIDTH / 2f;
    public static final float BLADE_LAUNCH_Y        = 120f;
    public static final float BLADE_WIDTH           = 24f;
    public static final float BLADE_HEIGHT          = 72f;

    // Stages
    public static final int TOTAL_STAGES            = 10;
    public static final int BASE_BLADES_PER_STAGE   = 5;

    // Score values
    public static final int SCORE_BLADE_HIT         = 10;
    public static final int SCORE_APPLE_BONUS        = 50;
    public static final int SCORE_STAGE_CLEAR_BONUS  = 100;

    // HUD sizes
    public static final float HUD_ICON_SIZE         = 26f;
    public static final float PAUSE_BTN_SIZE        = 56f;

    // Button sizes (world units, matching FIGMA_BRIEF)
    public static final float BTN_W_MAIN            = 260f;
    public static final float BTN_H_MAIN            = 60f;
    public static final float BTN_W_SECONDARY       = 260f;
    public static final float BTN_H_SECONDARY       = 52f;
    public static final float BTN_W_SMALL           = 120f;
    public static final float BTN_H_SMALL           = 44f;
    public static final float BTN_W_ROUND           = 60f;
    public static final float BTN_H_ROUND           = 60f;
    public static final float BTN_W_STAGE           = 180f;
    public static final float BTN_H_STAGE           = 90f;
    public static final float BTN_W_SETTINGS        = 320f;
    public static final float BTN_H_SETTINGS        = 56f;

    // Font sizes
    public static final int FONT_SIZE_TITLE         = 56;
    public static final int FONT_SIZE_BODY          = 28;
    public static final int FONT_SIZE_SMALL         = 20;
    public static final int FONT_SIZE_SCORE         = 44;

    // SharedPreferences keys
    public static final String PREFS_NAME           = "GamePrefs";
    public static final String PREF_MUSIC           = "musicEnabled";
    public static final String PREF_SFX             = "sfxEnabled";
    public static final String PREF_VIBRATION       = "vibrationEnabled";
    public static final String PREF_DIFFICULTY      = "difficulty";
    public static final String PREF_HIGH_SCORE      = "highScore";
    public static final String PREF_UNLOCKED_STAGES = "unlockedStages";
    public static final String PREF_LEADERBOARD_PREFIX = "lb_";

    // Difficulty
    public static final int DIFFICULTY_EASY         = 0;
    public static final int DIFFICULTY_NORMAL       = 1;
    public static final int DIFFICULTY_HARD         = 2;

    // Leaderboard
    public static final int LEADERBOARD_SIZE        = 7;
}
