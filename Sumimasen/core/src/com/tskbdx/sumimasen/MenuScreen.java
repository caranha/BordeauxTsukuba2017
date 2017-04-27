package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Sydpy on 4/27/17.
 */
public class MenuScreen implements Screen {

    private Sumimasen game;

    public MenuScreen(Sumimasen game) {
        this.game = game;
        Gdx.input.setInputProcessor(new MenuInputProcessor(this));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
