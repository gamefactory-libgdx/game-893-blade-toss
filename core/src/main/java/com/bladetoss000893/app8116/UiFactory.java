package com.bladetoss000893.app8116;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UiFactory {

    public static TextButton.TextButtonStyle makeRectStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font  = font;
        s.up    = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_rectangle_depth_gradient.png", Texture.class)));
        s.down  = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_rectangle_depth_flat.png", Texture.class)));
        return s;
    }

    public static TextButton.TextButtonStyle makeRoundStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font  = font;
        s.up    = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_round_depth_gradient.png", Texture.class)));
        s.down  = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_round_depth_flat.png", Texture.class)));
        return s;
    }

    public static TextButton makeButton(String label, TextButton.TextButtonStyle style, float w, float h) {
        TextButton btn = new TextButton(label, style);
        btn.setSize(w, h);
        return btn;
    }
}
