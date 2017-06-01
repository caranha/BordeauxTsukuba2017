package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;

/*
 * Created by viet khang on 01/06/2017.
 */
public class FiredOnFirstDay extends Scene {
    @Override
    public void init() {
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

    }

    @Override
    protected String defaultMap() {
        return null;
    }

    @Override
    protected String defaultSpawn() {
        return null;
    }

    @Override
    public String description() {
        return "I am fired :(";
    }
}
