package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Sydpy on 4/27/17.
 */
public interface Scene {

    void init();
    void update(float dt);
    void render(Batch batch);

    boolean isFinished();
    Scene getNextScene();

    void dispose();
}