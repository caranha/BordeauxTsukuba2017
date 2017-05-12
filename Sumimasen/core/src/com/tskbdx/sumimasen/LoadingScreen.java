package com.tskbdx.sumimasen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Sydpy on 4/27/17.
 */
public class LoadingScreen implements Screen {

    private final static String[] imageNames = {
                    "player", "cab", "chatbubble", "entity", "item",
                    "key", "left", "right", "down", "up", "machine",
                    "magnet", "noname", "cat", "none16" };
    // to do : same with other ressources

    private final AssetManager manager;
    private final Sumimasen game;

    public LoadingScreen(Sumimasen game, AssetManager assetManager) {
        this.game = game;
        manager = assetManager;
        for (String name : imageNames) {
            manager.load("images/" + name + ".png", Texture.class);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        manager.update();
        float progression = manager.getProgress();
        if (progression == 1) {
            // we are done loading, let's move to game screen !
            game.setScreen(new GameScreen(game));
        }
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
}
