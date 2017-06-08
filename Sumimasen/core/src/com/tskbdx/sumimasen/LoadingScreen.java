package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.File;

/*
 * Created by Sydpy on 4/27/17.
 */
public class LoadingScreen implements Screen {

    private final AssetManager manager;
    private final Sumimasen game;
    private final Stage stage = new Stage();
    private ProgressBar bar;

    LoadingScreen(Sumimasen game) {
        this.game = game;
        manager = Sumimasen.getAssetManager();

        loadImages("images/");
        loadSkin("skin/skin/cloud-form-ui.json");

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

    private void loadSkin(String path) {
        manager.load(path, Skin.class);
    }

    private void loadImages(String path) {

        File root = new File(path);
        File[] list = root.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    loadImages(f.getAbsolutePath());
                } else if (f.getName().endsWith(".png")) {
                    manager.load(path + f.getName(), Texture.class);
                }
            }
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        manager.update();
        float progression = manager.getProgress();

        if (progression == 1.f) {
            // we are done loading, let's move to menu screen !
            game.setScreen(new MenuScreen(game));
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
