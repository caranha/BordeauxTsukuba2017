package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;

/**
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private Texture image = new Texture("badlogic.jpg");
    private int x = 0, y = 0;


    @Override
    public void init() {
        Gdx.input.setInputProcessor(new BasicInputProcessor(this));
    }

    @Override
    public void update(float dt) {
        x += 100*dt;
        y += 100*dt;
    }

    @Override
    public void render(Batch batch) {
        batch.draw(image, x, y);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Scene getNextScene() {
        return null;
    }

    @Override
    public void dispose() {
        image.dispose();
    }
}
