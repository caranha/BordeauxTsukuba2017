package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Sydpy on 4/27/17.
 */
public class LoadingScreen implements Screen {

    private final static String[] imageNames = {
            "player", "chatbubble", "entity", "item",
            "left", "right", "down", "up"
            , "noname", "cat"};
    // to do : same with other ressources

    private final AssetManager manager;
    private final Sumimasen game;
    private final Stage stage = new Stage();
    private ProgressBar bar;

    public LoadingScreen(Sumimasen game) {
        this.game = game;
        manager = Sumimasen.getAssetManager();
        for (String name : imageNames) {
            manager.load("images/" + name + ".png", Texture.class);
        }

        // Settings of the progress bar
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture progressBarBackground = new Texture(pixmap);

        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        Texture progressBarForeground = new Texture(pixmap);

        float progressBarWidth = Gdx.graphics.getWidth() * 0.75f,
                progressBarHeight = Gdx.graphics.getHeight() * 0.1f;

        // Add a new progress bar from the settings
        stage.addActor(createProgressBar(progressBarBackground, progressBarForeground,
                progressBarWidth, progressBarHeight));
    }

    @Override
    public void render(float delta) {
        manager.update();
        float progression = manager.getProgress();

        if (progression == 1.f) {
            // we are done loading, let's move to game screen !
            game.setScreen(new GameScreen(game));
        }

        bar.setValue(progression);
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Create a centered progress bar
     */
    private ProgressBar createProgressBar(Texture background, Texture foreground, float width, float height) {
        TextureRegionDrawable textureBar = new TextureRegionDrawable(new TextureRegion(background));
        ProgressBarStyle barStyle = new ProgressBarStyle(new TextureRegionDrawable(new TextureRegion(foreground)),
                textureBar);
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0.f, 10.f, 0.01f, false, barStyle);
        bar.setSize(width, height);
        bar.setPosition((Gdx.graphics.getWidth() - width) / 2.f, (Gdx.graphics.getHeight() - height) / 2.f);
        bar.setRange(0.f, 1.f);
        return bar;
    }
}
